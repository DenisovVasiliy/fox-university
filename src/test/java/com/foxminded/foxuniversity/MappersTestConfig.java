package com.foxminded.foxuniversity;

import com.foxminded.foxuniversity.dao.implementation.CourseDaoPostgres;
import com.foxminded.foxuniversity.dao.implementation.GroupDaoPostgres;
import com.foxminded.foxuniversity.dao.implementation.TeacherDaoPostgres;
import org.mockito.Mockito;
import org.springframework.context.annotation.Primary;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

@Configuration
@ComponentScan({"com.foxminded.foxuniversity"})
@PropertySource("classpath:database.properties")
public class MappersTestConfig {

    @Bean
    @Primary
    public CourseDaoPostgres courseDao() {
        return Mockito.mock(CourseDaoPostgres.class);
    }

    @Bean
    @Primary
    public GroupDaoPostgres groupDao() {
        return Mockito.mock(GroupDaoPostgres.class);
    }

    @Bean
    @Primary
    public TeacherDaoPostgres teacherDao() {
        return Mockito.mock(TeacherDaoPostgres.class);
    }
}
