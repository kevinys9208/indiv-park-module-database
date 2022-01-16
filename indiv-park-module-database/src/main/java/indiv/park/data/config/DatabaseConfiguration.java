package indiv.park.data.config;

import java.util.Map;

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

	public String getName() {
		return name;
	}

	public String getType() {
		return type;
	}

	public String getIp() {
		return ip;
	}

	public String getSid() {
		return sid;
	}

	public String getUser() {
		return user;
	}

	public String getPassword() {
		return password;
	}

	public int getPort() {
		return port;
	}
}