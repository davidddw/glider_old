package com.yunshan.common;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import javax.sql.DataSource;

import org.apache.ibatis.jdbc.ScriptRunner;
import org.apache.ibatis.mapping.Environment;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.transaction.jdbc.JdbcTransactionFactory;
import org.apache.logging.log4j.Logger;
import org.mybatis.guice.MyBatisModule;
import org.mybatis.guice.datasource.builtin.PooledDataSourceProvider;
import org.mybatis.guice.datasource.helper.JdbcHelper;
import org.yaml.snakeyaml.Yaml;

import com.google.inject.Binder;
import com.google.inject.Guice;
import com.google.inject.Injector;
import com.google.inject.Module;
import com.google.inject.name.Names;
import com.yunshan.database.domain.Domain;
import com.yunshan.database.domain.ProductSpec;
import com.yunshan.database.domain.User;
import com.yunshan.database.mapper.DomainMapper;
import com.yunshan.database.mapper.ProductSpecMapper;
import com.yunshan.database.mapper.UserMapper;
import com.yunshan.database.service.LivecloudService;
import com.yunshan.database.service.LivecloudServiceImpl;
import com.yunshan.utils.Util;

public abstract class LivecloudBase {

	protected static final Logger s_logger = Util.getLogger();
	
	protected Map<String, String> uuidMapper;
	
	private Injector injector;
	
	private LivecloudService livecloudService;

	public LivecloudBase(String filename) throws Exception {
		setupMyBatisGuice();
	}

	protected static String getMapValueSafely(Map<String, String> map, String key) {
		if (map.containsKey(key)) {
			return map.get(key);
		} else {
			s_logger.error("No properly uuid in the database");
			System.exit(1);
			return null;
		}
	}
	
	public void setupMyBatisGuice() throws Exception {
		// bindings
        List<Module> modules = this.createMyBatisModule();
        this.injector = Guice.createInjector(modules);

        Environment environment = this.injector.getInstance(SqlSessionFactory.class).getConfiguration().getEnvironment();
        DataSource dataSource = environment.getDataSource();
        ScriptRunner runner = new ScriptRunner(dataSource.getConnection());
        runner.setAutoCommit(true);
        runner.setStopOnError(true);
        runner.closeConnection();

        this.livecloudService = this.injector.getInstance(LivecloudService.class);
    }
	
	protected static Properties createTestProperties() {
        final Properties myBatisProperties = new Properties();
        myBatisProperties.setProperty("mybatis.environment.id", "test");
        myBatisProperties.setProperty("JDBC.host", "10.33.37.28");
        myBatisProperties.setProperty("JDBC.port", "20130");
        myBatisProperties.setProperty("JDBC.schema", "livecloud");
        myBatisProperties.setProperty("JDBC.username", "cloud");
        myBatisProperties.setProperty("JDBC.password", "yunshan3302");
        myBatisProperties.setProperty("JDBC.autoCommit", "false");
        return myBatisProperties;
    }
	
	protected List<Module> createMyBatisModule() {
        List<Module> modules = new ArrayList<Module>();
        modules.add(JdbcHelper.MySQL);
        modules.add(new MyBatisModule() {

            @Override
            protected void initialize() {
                bindDataSourceProviderType(PooledDataSourceProvider.class);
                bindTransactionFactoryType(JdbcTransactionFactory.class);
                addMapperClass(UserMapper.class);
                addMapperClass(DomainMapper.class);
                addMapperClass(ProductSpecMapper.class);
            }

        });
        modules.add(new Module() {
            @Override
            public void configure(Binder binder) {
            	Names.bindProperties(binder, createTestProperties());
                binder.bind(LivecloudService.class).to(LivecloudServiceImpl.class);
            }
        });

        return modules;
    }

	protected Map<String, String> initMapper() throws Exception {
		
		s_logger.info("Init Environment, get uuid from database. please wait...");
		Map<String, String> dbUuidMapper = new HashMap<String, String>();
		for (User user : livecloudService.getUsers()) {
			dbUuidMapper.put(user.getName(), user.getUuid());
		}
		for (Domain domain : livecloudService.getDomains()) {
			dbUuidMapper.put(domain.getName(), domain.getUuid());
		}
		for (ProductSpec ProductSpec : livecloudService.getProductSpecs()) {
			dbUuidMapper.put(ProductSpec.getPlanName(), ProductSpec.getUuid());
		}
		return dbUuidMapper;
	}

	protected <T> T getConfigFromYaml(String filename, Class<T> type) {
		try {
			Yaml yaml = new Yaml();
			InputStream stream = new FileInputStream(new File(filename));
			s_logger.info("Read config from yaml file: " + filename);
			return (T)yaml.loadAs(stream, type);
		} catch (FileNotFoundException e) {
			s_logger.error("Cannot find file " + filename, e);
		}
		return null;
	}
	
	protected int getUserIdFromDB(String username) throws Exception {
		for (User user : livecloudService.getUsers()) {
			if (username.equals(user.getName())) {
				return user.getId();
			}
		}
		return 0;
	}

	public abstract void build();
}
