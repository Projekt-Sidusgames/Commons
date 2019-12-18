package net.crytec.libs.commons.utils;

import java.time.Duration;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import org.apache.commons.lang3.time.DurationFormatUtils;

public class UtilTime {

  public static String getElapsedTime(final long timestamp) {
    return getElapsedTime(millisToLocalDate(timestamp));
  }


  public static int getElapsedTimeInSeconds(final long start) {
    return (int) Duration.between(millisToInstant(start), Instant.now()).toMillis() / 1000;
  }

  //TODO: Translatable
  public static String getElapsedTime(final LocalDateTime startdate) {

    final Duration d = Duration.between(localDateToInstant(startdate), localDateToInstant(LocalDateTime.now()));

    final StringBuilder builder = new StringBuilder();
    if (d.toDays() > 0) {
      builder.append(d.toDays()).append((d.toDays() > 1) ? " Tage " : " Tag ");
    }
    if (d.toHours() % 24 > 0) {
      builder.append(d.toHours() % 24).append((d.toHours() % 24 > 1) ? " Stunden " : " Stunde ");
    }
    if (d.toMinutes() % 60 > 0) {
      builder.append(d.toMinutes() % 60).append((d.toMinutes() % 60 > 1) ? " Minuten " : " Minute ");
    }
    if (d.getSeconds() % 60 > 0) {
      builder.append(d.getSeconds() % 60).append((d.getSeconds() % 60 > 1) ? " Sekunden" : " Sekunde");
    }
    return builder.toString();
  }

  public static String between(final LocalDateTime startdate, final LocalDateTime endDate) {

    final Duration d = Duration.between(localDateToInstant(startdate), localDateToInstant(endDate));

    final StringBuilder builder = new StringBuilder();
    if (d.toDays() > 0) {
      builder.append(d.toDays()).append((d.toDays() > 1) ? " Tage " : " Tag ");
    }
    if (d.toHours() % 24 > 0) {
      builder.append(d.toHours() % 24).append((d.toHours() % 24 > 1) ? " Stunden " : " Stunde ");
    }
    if (d.toMinutes() % 60 > 0) {
      builder.append(d.toMinutes() % 60).append((d.toMinutes() % 60 > 1) ? " Minuten " : " Minute ");
    }
    if (d.getSeconds() % 60 > 0) {
      builder.append(d.getSeconds() % 60).append((d.getSeconds() % 60 > 1) ? " Sekunden" : " Sekunde");
    }
    return builder.toString();
  }

  public static String getTimeUntil(final long timestamp, final String expired) {
    return getTimeUntil(millisToLocalDate(timestamp), expired);
  }

  public static String getTimeUntil(final long timestamp) {
    return getTimeUntil(timestamp, null);
  }

  public static String getTimeUntil(final LocalDateTime endDate) {
    return getTimeUntil(endDate, null);
  }

  //TODO: Translatable
  public static String getTimeUntil(final LocalDateTime endDate, final String expired) {
    if (isElapsed(endDate)) {
      return expired != null ? expired : "Expired";
    }
    final Duration d = Duration.between(localDateToInstant(LocalDateTime.now()), localDateToInstant(endDate));

    final StringBuilder builder = new StringBuilder();
    if (d.toDays() > 0) {
      builder.append(d.toDays()).append((d.toDays() > 1) ? " Tage " : " Tag ");
    }
    if (d.toHours() % 24 > 0) {
      builder.append(d.toHours() % 24).append((d.toHours() % 24 > 1) ? " Stunden " : " Stunde ");
    }
    if (d.toMinutes() % 60 > 0) {
      builder.append(d.toMinutes() % 60).append((d.toMinutes() % 60 > 1) ? " Minuten " : " Minute ");
    }
    if (d.getSeconds() % 60 > 0) {
      builder.append(d.getSeconds() % 60).append((d.getSeconds() % 60 > 1) ? " Sekunden" : " Sekunde");
    }
    return builder.toString();
  }

  public static boolean isElapsed(final LocalDateTime endDate) {
    return LocalDateTime.now().isAfter(endDate);
  }

  public static boolean isElapsed(final long timestamp) {
    return isElapsed(millisToLocalDate(timestamp));
  }

  public static LocalDateTime millisToLocalDate(final long timestamp) {
    return Instant.ofEpochMilli(timestamp).atZone(ZoneId.systemDefault()).toLocalDateTime();
  }

  public static long LocalDateToMillis(final LocalDateTime timestamp) {
    return timestamp.toInstant(OffsetDateTime.now().getOffset()).toEpochMilli();
  }

  public static Instant millisToInstant(final long timestamp) {
    return Instant.ofEpochMilli(timestamp);
  }

  public static Instant localDateToInstant(final LocalDateTime timestamp) {
    return timestamp.toInstant(OffsetDateTime.now().getOffset());
  }

  public static LocalDateTime instantToLocalDate(final Instant instant) {
    return instant.atZone(ZoneId.systemDefault()).toLocalDateTime();
  }

  public static String now() {
    return DurationFormatUtils.formatDuration(LocalDateToMillis(LocalDateTime.now()), "HH:mm:ss", false);
  }

  public static String when(final LocalDateTime destinationDate) {
    return destinationDate.format(DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm:ss"));
  }

  public static String when(final long destinationTime) {
    return millisToLocalDate(destinationTime).format(DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm:ss"));
  }

  public static String date() {
    return LocalDate.now().format(DateTimeFormatter.ofPattern("dd.MM.yyyy"));
  }
}