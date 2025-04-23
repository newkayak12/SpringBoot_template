package com.base.config.persistence.p6spy;

import com.p6spy.engine.logging.Category;
import com.p6spy.engine.spy.appender.MessageFormattingStrategy;
import java.util.Locale;
import org.hibernate.engine.jdbc.internal.FormatStyle;

public class P6SpyFormatter implements MessageFormattingStrategy {

    private static final String CREATE = "create";
    private static final String ALTER = "alter";
    private static final String DROP = "drop";
    private static final String COMMENT = "comment";

    @Override
    public String formatMessage(
        int connectionId, String now, long elapsed,
        String category, String prepared, String sql, String url
    ) {

        StringBuilder builder = new StringBuilder();
        if (sql.trim().isEmpty()) {
            builder.append(this.formatByCommand(category));
        } else {
            builder.append(this.formatBySql(sql, category));
            builder.append(getAdditionalMessages(elapsed));
        }

        return builder.toString();
    }

    private String formatByCommand(String category) {
        return String.format(
            """
                ----------------------------------------------------------------------------------------------------
                Execute Command :
                %s
                ----------------------------------------------------------------------------------------------------
                """, category
        );
    }

    private String formatBySql(String sql, String category) {
        if (isStatementDDL(sql, category)) {
            return String.format(
                """
                    Execute DDL : %s
                    """
                , FormatStyle.DDL.getFormatter().format(sql)
            );


        } else {
            return String.format(
                """
                    Execute DML : %s
                    """, FormatStyle.BASIC.getFormatter().format(sql)
            );
        }
    }

    private String getAdditionalMessages(long elapsed) {
        return String.format(
            """

                %s
                ----------------------------------------------------------------------------------------------------
                """,
            String.format("Execution Time: %s ms", elapsed)
        );

    }

    private boolean isStatementDDL(String sql, String category) {
        return isStatement(category) && isDDL(sql.trim().toLowerCase(Locale.ROOT));
    }

    private boolean isStatement(String category) {
        return Category.STATEMENT.getName().equals(category);
    }

    private boolean isDDL(String lowerSql) {
        return lowerSql.startsWith(CREATE)
            || lowerSql.startsWith(ALTER)
            || lowerSql.startsWith(DROP)
            || lowerSql.startsWith(COMMENT);
    }
}
