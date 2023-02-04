package indiv.park.data.config;

import lombok.Getter;

import java.util.Map;


@Getter
public class DatabaseConfiguration {

	private String name, type, ip, sid, user, password;
	private int port;

	public DatabaseConfiguration(Map<String, Object> config) {
		this.name = (String) config.get("name");
		this.type = (String) config.get("type");
		this.ip = (String) config.get("ip");
		this.sid = (String) config.get("sid");
		this.user = (String) config.get("user");
		this.password = (String) config.get("password");
		this.port = (int) config.get("port");
	}
}