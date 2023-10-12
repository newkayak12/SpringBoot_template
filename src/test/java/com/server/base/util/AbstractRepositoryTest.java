package com.server.base.util;

import com.server.base.components.configure.queryDsl.Config;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.Profile;
import org.springframework.data.repository.config.BootstrapMode;

@DataJpaTest(bootstrapMode = BootstrapMode.DEFAULT, showSql = true)
@Import(Config.class)
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
//@ComponentScan(includeFilters = {
//        @ComponentScan.Filter(
//                type = FilterType.ANNOTATION,
//                classes = {Configuration.class, Repository.class}
//        )
//})
@Profile("local")
public abstract class AbstractRepositoryTest {
}
