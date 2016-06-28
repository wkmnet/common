import ch.qos.logback.core.ConsoleAppender
import ch.qos.logback.classic.encoder.PatternLayoutEncoder

import static ch.qos.logback.classic.Level.INFO

StringBuilder p = new StringBuilder();
p.append("Commons %d{yyyy-MM-dd/HH:mm:ss} ");
p.append("%-5level %thread ");
p.append("%class %line 【");
p.append("%msg");
p.append("】");
p.append("%n");

appender("CONSOLE", ConsoleAppender) {
    encoder(PatternLayoutEncoder) {
        pattern = p.toString();
    }
}

root(INFO, ["CONSOLE"])