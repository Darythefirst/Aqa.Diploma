package ru.netology.data;

import com.github.javafaker.Faker;
import lombok.Value;
import org.apache.commons.lang3.RandomStringUtils;

import java.security.SecureRandom;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Locale;
import java.util.Random;


public class DataHelper {
    private static final Random RND = new SecureRandom();
    private static final String rusLetter = "АБВГДЕЁЖЗИЙКЛМНОПРСТУФХЦЧШЩЪЫЬЭЮЯ";

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

    public static String getShortCardNumber() {
        return ("4444 4441");
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
        return faker.name().firstName().replaceAll("[^A-Za-z]", "") + " " + faker.name().lastName().replaceAll("[^A-Za-z]", "");
    }

    public static String generateRussianOwner() {
        Faker faker1 = new Faker(new Locale("ru"));
        return faker1.name().firstName() + " " + faker1.name().lastName();
    }

    public static String generateDoubleNameOwner() {
        return faker.name().firstName() + "-" + faker.name().firstName() + " " + faker.name().lastName();
    }

    public static String generateDoubleLastNameOwner() {
        return faker.name().firstName() + " " + faker.name().lastName() + "-" + faker.name().lastName();
    }

    public static String generateDashAfterNameOwner() {
        return faker.name().firstName() + "-" + " " + faker.name().lastName();
    }

    public static String generateDashBeforeLastNameOwner() {
        return faker.name().firstName() + " " + "-" + faker.name().lastName();
    }

    public static String generateSecurityCode(int digits, boolean strict) {
        return String.valueOf(faker.number().randomNumber(digits, strict));
    }

    public static String generateSymbol(int min, int max) {
        return RandomStringUtils.randomAscii(min, max);
    }

    public static String generateDash(int length) {
        return RandomStringUtils.randomAlphabetic(length).replaceAll("[A-Za-z]", "-");
    }

    public static String generateSpace(int length) {
        return RandomStringUtils.randomAlphabetic(length).replaceAll("[A-Za-z]", " ");
    }

    public static String generateRandomRusLetter(int length) {
        return RandomStringUtils.random(length, "АБВГДЕЁЖЗИЙКЛМНОПРСТУФХЦЧШЩЪЫЬЭЮЯ");
    }

    public static String generateZeroLetter(int length) {
        return RandomStringUtils.random(length, "0");
    }

    public static String generateRandomLatinLetter(int length) {
        return RandomStringUtils.randomAlphabetic(length);
    }

}
