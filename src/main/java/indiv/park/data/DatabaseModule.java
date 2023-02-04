package indiv.park.data;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import javax.persistence.Entity;

import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;
import org.reflections.Reflections;

import indiv.park.data.config.DatabaseConfiguration;
import indiv.park.data.exception.NameNotFoundException;
import indiv.park.data.exception.SameNameException;
import indiv.park.data.exception.TypeNotFoundException;
import indiv.park.starter.annotation.Module;
import indiv.park.starter.exception.ModuleException;
import indiv.park.starter.inheritance.ModuleBase;
import lombok.extern.slf4j.Slf4j;

@Module(name = "database")
@Slf4j
public final class DatabaseModule implements ModuleBase {
	
	public static final DatabaseModule INSTANCE = new DatabaseModule();

	private final List<DatabaseConfiguration> databaseList = new ArrayList<>();
	private final Map<String, SessionFactory> sessionFactoryMap = new ConcurrentHashMap<>();
	
	private Object configuration;
	
	private DatabaseModule() {}
	
	@Override
	public void initialize(Class<?> mainClass) {
		if (configuration == null) {
			throw new ModuleException("데이터베이스를 구성하기 위한 설정 정보가 없습니다.", null);
		}
		
		addUserDatabaseConfiguration();
		createSessionFactory(mainClass);
	}

	@Override
	public void setConfiguration(Object configuration) {
		this.configuration = configuration;
	}
	
	@SuppressWarnings("unchecked")
	private void addUserDatabaseConfiguration() {
		List<Map<String, Object>> databaseConfigList = (List<Map<String, Object>>) configuration;
		databaseConfigList.forEach(config -> databaseList.add(new DatabaseConfiguration(config)));
	}
	
	private void createSessionFactory(Class<?> mainClass) {
		databaseList.forEach(config -> readDatabaseConfiguration(config, mainClass));
	}

	private void readDatabaseConfiguration(DatabaseConfiguration config, Class<?> mainClass) {
		DataSource dataSource = DataSource.getDataSourceByName(config.getType());
		
		Configuration configuration = new Configuration();
		
		configuration.setProperty("hibernate.connection.provider_class", "org.hibernate.hikaricp.internal.HikariCPConnectionProvider");
		configuration.setProperty("hibernate.dialect", dataSource.getDiarect());
		configuration.setProperty("hibernate.hikari.poolName", config.getName());
		configuration.setProperty("hibernate.hikari.minimumIdle", "5");
		configuration.setProperty("hibernate.hikari.maximumPoolSize", "10");
		configuration.setProperty("hibernate.hikari.idleTimeout", "30000");
		configuration.setProperty("hibernate.hikari.dataSourceClassName", dataSource.getDataSourceClassName());
		configuration.setProperty("hibernate.hikari.dataSource.url", dataSource.getUrl(config.getIp(), config.getPort(), config.getSid()));
		
		if (dataSource.name().equals("SQLITE")) {
			configuration.setProperty("hibernate.hbm2ddl.auto", "validate");
			
		} else {
			configuration.setProperty("hibernate.hikari.dataSource.user", config.getUser());
			configuration.setProperty("hibernate.hikari.dataSource.password", config.getPassword());
		}
		
		Reflections reflections = new Reflections(mainClass.getPackage().getName());
		Set<Class<?>> entitySet = reflections.getTypesAnnotatedWith(Entity.class);
		final String found = "확인된 엔티티 : {}";
		
		for (Class<?> clazz : entitySet) {
			logger.info(found, clazz.getSimpleName());
			configuration.addAnnotatedClass(clazz);
		}
		
		SessionFactory sessionFactory = configuration.buildSessionFactory();
		
		if (sessionFactoryMap.containsKey(config.getName())) {
			throw new SameNameException(config.getName());
		}
		sessionFactoryMap.put(config.getName(), sessionFactory);
	}
	
	public SessionFactory getSessionFactory(String name) {
		SessionFactory sessionFactory = sessionFactoryMap.get(name);
		if (sessionFactory == null) {
			throw new NameNotFoundException(name);
		}
		return sessionFactory;
	}
	
	public void closeGracefully() {
		sessionFactoryMap.forEach((name, sessionFactory) -> {
			if (Objects.nonNull(sessionFactory)) {
				sessionFactory.close();
			}
		});
		
		logger.info("데이터베이스를 안전하게 종료하였습니다.");
	}
}
