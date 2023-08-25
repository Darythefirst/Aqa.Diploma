package ru.netology.data;

import com.github.javafaker.Faker;
import lombok.Value;
import org.apache.commons.lang3.RandomStringUtils;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Locale;


public class DataHelper {
    public DataHelper() {
    }

    private static final Faker faker = new Faker(new Locale("en"));

    @Value
    public static class OwnerCardInfo {
        String card;
        String month;
        String year;
        String owner;
        String securityCode;
    }

    public static CardInfo getFirstCardInfo() {
        return new CardInfo("4444 4444 4444 4441", "APPROVED");
    }

    public static CardInfo getSecondCardInfo() {
        return new CardInfo("4444 4444 4444 4442", "DECLINED");
    }

    @Value
    public static class CardInfo {
        String cardNumber;
        String cardStatus;
    }

    public static String generateValidMonth(int shift, String pattern) {
        return LocalDate.now().plusMonths(shift).format(DateTimeFormatter.ofPattern(pattern));
    }

    public static String generateInvalidMonth() {
        return String.valueOf(faker.number().numberBetween(13, 99));
    }

    public static String generateYear(int shift, String pattern) {
        return LocalDate.now().plusYears(shift).format(DateTimeFormatter.ofPattern(pattern));
    }

    public static String generateOwner() {
        return faker.name().firstName() + " " + faker.name().lastName();
    }

    public static String generateInvalidOwner(int length) {
        return RandomStringUtils.randomAlphabetic(length);
    }

    public static String generateRussianOwner() {
        Faker faker1 = new Faker(new Locale("ru"));
        return faker1.name().firstName() + " " + faker1.name().lastName();
    }

    public static String generateSecurityCode(int digits, boolean strict) {
        return String.valueOf(faker.number().randomNumber(digits, strict));
    }

}
