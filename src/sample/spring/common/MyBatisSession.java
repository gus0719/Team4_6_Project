package sample.spring.common;

import java.io.IOException;
import java.io.Reader;

import org.apache.ibatis.io.Resources;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;

public class MyBatisSession {
	static SqlSession sqlSession;
	public static SqlSession getSession(String res) {
		try {
			String resource = res;
			Reader reader = Resources.getResourceAsReader(resource);
			SqlSessionFactory sqlFactory = new SqlSessionFactoryBuilder().build(reader);
			sqlSession = sqlFactory.openSession();
			return sqlSession;
		} catch(IOException e) {
			e.printStackTrace();
		}
		return sqlSession;
	}
}