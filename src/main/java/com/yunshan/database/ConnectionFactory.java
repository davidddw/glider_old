package com.yunshan.database;

import java.io.InputStream;
import java.io.Reader;
import java.util.Properties;

import org.apache.ibatis.io.Resources;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;

public class ConnectionFactory {
	private static SqlSessionFactory sqlMapper;
	private static Reader reader;
	private static InputStream input;

	static {
		try {
			reader = Resources.getResourceAsReader("configuration.xml");
			Properties prop = new Properties();
			input = Resources.getResourceAsStream("database.properties");
			prop.load(input);
			sqlMapper = new SqlSessionFactoryBuilder().build(reader, prop);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static SqlSessionFactory getSession() {
		return sqlMapper;
	}
}
