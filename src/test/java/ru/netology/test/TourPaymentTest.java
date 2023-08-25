package ru.netology.test;

import com.codeborne.selenide.logevents.SelenideLogger;
import io.qameta.allure.selenide.AllureSelenide;
import org.junit.jupiter.api.*;
import ru.netology.data.DataHelper;
import ru.netology.data.SQLHelper;
import ru.netology.page.PaymentPage;
import ru.netology.page.TourPage;


import static com.codeborne.selenide.Selenide.open;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static ru.netology.data.SQLHelper.cleanDB;

public class TourPaymentTest {
    @BeforeAll
    static void setUpAll() {
        SelenideLogger.addListener("allure", new AllureSelenide());
    }

    @AfterAll
    static void tearDownAll() {
        SelenideLogger.removeListener("allure");
    }

    @BeforeEach
    void setup() {
        open("http://localhost:8080");
    }

    @AfterAll
    static void close() {
        cleanDB();
    }

    @Test
    @DisplayName("Сценарий №1. Успешная покупка тура при вводе валидных значений во все поля")
    void allFieldsValid() {
        var tourPage = new TourPage();
        var validCard = DataHelper.getFirstCardInfo().getCardNumber();
        var validMonth = DataHelper.generateValidMonth(0, "MM");
        var validYear = DataHelper.generateYear(0, "yy");
        var validOwner = DataHelper.generateOwner();
        var validCode = DataHelper.generateSecurityCode(3, true);
        DataHelper.OwnerCardInfo ownerCardInfo = new DataHelper.OwnerCardInfo(validCard, validMonth, validYear, validOwner, validCode);
        tourPage.clickBuyButton().orderTourPayment(ownerCardInfo);
        var paymentPage = new PaymentPage();
        paymentPage.findNotification();
        assertEquals(DataHelper.getFirstCardInfo().getCardStatus(), SQLHelper.getPaymentStatus());
    }

    @Test
    @DisplayName("Сценарий №2. Успешная покупка тура при вводе валидных значений во все поля, год увеличен на 5 лет")
    void validYearBiggerThanCurrent() {
        var tourPage = new TourPage();
        var validCard = DataHelper.getFirstCardInfo().getCardNumber();
        var validMonth = DataHelper.generateValidMonth(0, "MM");
        var validYear = DataHelper.generateYear(5, "yy");
        var validOwner = DataHelper.generateOwner();
        var validCode = DataHelper.generateSecurityCode(3, true);
        DataHelper.OwnerCardInfo ownerCardInfo = new DataHelper.OwnerCardInfo(validCard, validMonth, validYear, validOwner, validCode);
        tourPage.clickBuyButton().orderTourPayment(ownerCardInfo);
        var paymentPage = new PaymentPage();
        paymentPage.findNotification();
        assertEquals(DataHelper.getFirstCardInfo().getCardStatus(), SQLHelper.getPaymentStatus());
    }

    @Test
    @DisplayName("Сценарий №3. Успешная покупка тура при вводе валидных значений, фамилия имеет дефис(двойная фамилия)")
    void validDoubleSurname() {
        var tourPage = new TourPage();
        var validCard = DataHelper.getFirstCardInfo().getCardNumber();
        var validMonth = DataHelper.generateValidMonth(0, "MM");
        var validYear = DataHelper.generateYear(0, "yy");
        var validOwner = "Anton Antonov-Ivanov";
        var validCode = DataHelper.generateSecurityCode(3, true);
        DataHelper.OwnerCardInfo ownerCardInfo = new DataHelper.OwnerCardInfo(validCard, validMonth, validYear, validOwner, validCode);
        tourPage.clickBuyButton().orderTourPayment(ownerCardInfo);
        var paymentPage = new PaymentPage();
        paymentPage.findNotification();
        assertEquals(DataHelper.getFirstCardInfo().getCardStatus(), SQLHelper.getPaymentStatus());
    }

    @Test
    @DisplayName("Сценарий №4. Успешная покупка тура при вводе валидных значений, имя имеет дефис (двойное имя)")
    void validDoubleName() {
        var tourPage = new TourPage();
        var validCard = DataHelper.getFirstCardInfo().getCardNumber();
        var validMonth = DataHelper.generateValidMonth(0, "MM");
        var validYear = DataHelper.generateYear(0, "yy");
        var validOwner = "Anna-Maria Ivanova";
        var validCode = DataHelper.generateSecurityCode(3, true);
        DataHelper.OwnerCardInfo ownerCardInfo = new DataHelper.OwnerCardInfo(validCard, validMonth, validYear, validOwner, validCode);
        tourPage.clickBuyButton().orderTourPayment(ownerCardInfo);
        var paymentPage = new PaymentPage();
        paymentPage.findNotification();
        assertEquals(DataHelper.getFirstCardInfo().getCardStatus(), SQLHelper.getPaymentStatus());
    }

    @Test
    @DisplayName("Сценарий №5. Покупка тура при вводе невалидного номера карты")
    void invalidCardNumber() {
        var tourPage = new TourPage();
        var card = DataHelper.getSecondCardInfo().getCardNumber();
        var month = DataHelper.generateValidMonth(0, "MM");
        var year = DataHelper.generateYear(0, "yy");
        var owner = DataHelper.generateOwner();
        var code = DataHelper.generateSecurityCode(3, true);
        DataHelper.OwnerCardInfo ownerCardInfo = new DataHelper.OwnerCardInfo(card, month, year, owner, code);
        tourPage.clickBuyButton().orderTourPayment(ownerCardInfo);
        var paymentPage = new PaymentPage();
        paymentPage.findNotificationError();
        assertEquals(DataHelper.getSecondCardInfo().getCardStatus(), SQLHelper.getPaymentStatus());
    }

    @Test
    @DisplayName("Сценарий №6. Покупка тура при неполном вводе номера карты")
    void invalidCardShortNumber() {
        var tourPage = new TourPage();
        var card = "4444 4444";
        var month = DataHelper.generateValidMonth(0, "MM");
        var year = DataHelper.generateYear(0, "yy");
        var owner = DataHelper.generateOwner();
        var code = DataHelper.generateSecurityCode(3, true);
        DataHelper.OwnerCardInfo ownerCardInfo = new DataHelper.OwnerCardInfo(card, month, year, owner, code);
        tourPage.clickBuyButton().orderTourPayment(ownerCardInfo);
        var paymentPage = new PaymentPage();
        paymentPage.findCardError();
    }

    @Test
    @DisplayName("Сценарий №7. Покупка тура при вводе невалидного номера карты, состоящего из нулей")
    void invalidCardZeroNumber() {
        var tourPage = new TourPage();
        var card = "0000 0000 0000 0000";
        var month = DataHelper.generateValidMonth(0, "MM");
        var year = DataHelper.generateYear(0, "yy");
        var owner = DataHelper.generateOwner();
        var code = DataHelper.generateSecurityCode(3, true);
        DataHelper.OwnerCardInfo ownerCardInfo = new DataHelper.OwnerCardInfo(card, month, year, owner, code);
        tourPage.clickBuyButton().orderTourPayment(ownerCardInfo);
        var paymentPage = new PaymentPage();
        paymentPage.findCardError();
    }

    @Test
    @DisplayName("Сценарий №8. Покупка тура при вводе пробелов в поле ввода номера карты")
    void invalidSpaceCardNumber() {
        var tourPage = new TourPage();
        var card = "  ";
        var month = DataHelper.generateValidMonth(0, "MM");
        var year = DataHelper.generateYear(0, "yy");
        var owner = DataHelper.generateOwner();
        var code = DataHelper.generateSecurityCode(3, true);
        DataHelper.OwnerCardInfo ownerCardInfo = new DataHelper.OwnerCardInfo(card, month, year, owner, code);
        tourPage.clickBuyButton().orderTourPayment(ownerCardInfo);
        var paymentPage = new PaymentPage();
        paymentPage.findCardError();
    }

    @Test
    @DisplayName("Сценарий №9. Покупка тура при вводе спецсимволов в поле ввода номера карты")
    void invalidSymbolCardNumber() {
        var tourPage = new TourPage();
        var card = "%^&*";
        var month = DataHelper.generateValidMonth(0, "MM");
        var year = DataHelper.generateYear(0, "yy");
        var owner = DataHelper.generateOwner();
        var code = DataHelper.generateSecurityCode(3, true);
        DataHelper.OwnerCardInfo ownerCardInfo = new DataHelper.OwnerCardInfo(card, month, year, owner, code);
        tourPage.clickBuyButton().orderTourPayment(ownerCardInfo);
        var paymentPage = new PaymentPage();
        paymentPage.findCardError();
    }

    @Test
    @DisplayName("Сценарий №10. Покупка тура при вводе латинских букв в поле ввода номера карты")
    void invalidLatinLetterCardNumber() {
        var tourPage = new TourPage();
        var card = "GH44 4444 4444 4441";
        var month = DataHelper.generateValidMonth(0, "MM");
        var year = DataHelper.generateYear(0, "yy");
        var owner = DataHelper.generateOwner();
        var code = DataHelper.generateSecurityCode(3, true);
        DataHelper.OwnerCardInfo ownerCardInfo = new DataHelper.OwnerCardInfo(card, month, year, owner, code);
        tourPage.clickBuyButton().orderTourPayment(ownerCardInfo);
        var paymentPage = new PaymentPage();
        paymentPage.findCardError();
    }

    @Test
    @DisplayName("Сценарий №11 Покупка тура при вводе русских букв в поле ввода номера карты")
    void invalidRussianLetterCardNumber() {
        var tourPage = new TourPage();
        var card = "ПЧ44 4444 4444 4441";
        var month = DataHelper.generateValidMonth(0, "MM");
        var year = DataHelper.generateYear(0, "yy");
        var owner = DataHelper.generateOwner();
        var code = DataHelper.generateSecurityCode(3, true);
        DataHelper.OwnerCardInfo ownerCardInfo = new DataHelper.OwnerCardInfo(card, month, year, owner, code);
        tourPage.clickBuyButton().orderTourPayment(ownerCardInfo);
        var paymentPage = new PaymentPage();
        paymentPage.findCardError();
    }

    @Test
    @DisplayName("Сценарий №12. Покупка тура при вводе дефиса в поле ввода номера карты")
    void invalidDashCardNumber() {
        var tourPage = new TourPage();
        var card = "---- 4444 4444 4441";
        var month = DataHelper.generateValidMonth(0, "MM");
        var year = DataHelper.generateYear(0, "yy");
        var owner = DataHelper.generateOwner();
        var code = DataHelper.generateSecurityCode(3, true);
        DataHelper.OwnerCardInfo ownerCardInfo = new DataHelper.OwnerCardInfo(card, month, year, owner, code);
        tourPage.clickBuyButton().orderTourPayment(ownerCardInfo);
        var paymentPage = new PaymentPage();
        paymentPage.findCardError();
    }

    @Test
    @DisplayName("Сценарий №13. Покупка тура при вводе невалидного месяца текущего года")
    void invalidSmallerMonth() {
        var tourPage = new TourPage();
        var card = DataHelper.getFirstCardInfo().getCardNumber();
        var month = DataHelper.generateValidMonth(-1, "MM");
        var year = DataHelper.generateYear(0, "yy");
        var owner = DataHelper.generateOwner();
        var code = DataHelper.generateSecurityCode(3, true);
        DataHelper.OwnerCardInfo ownerCardInfo = new DataHelper.OwnerCardInfo(card, month, year, owner, code);
        tourPage.clickBuyButton().orderTourPayment(ownerCardInfo);
        var paymentPage = new PaymentPage();
        paymentPage.findMonthError();
    }

    @Test
    @DisplayName("Сценарий №14. Покупка тура при вводе невалидного значения месяца, больше допустимого")
    void invalidLargeMonth() {
        var tourPage = new TourPage();
        var card = DataHelper.getFirstCardInfo().getCardNumber();
        var month = DataHelper.generateInvalidMonth();
        var year = DataHelper.generateYear(0, "yy");
        var owner = DataHelper.generateOwner();
        var code = DataHelper.generateSecurityCode(3, true);
        DataHelper.OwnerCardInfo ownerCardInfo = new DataHelper.OwnerCardInfo(card, month, year, owner, code);
        tourPage.clickBuyButton().orderTourPayment(ownerCardInfo);
        var paymentPage = new PaymentPage();
        paymentPage.findMonthError();
    }

    @Test
    @DisplayName("Сценарий №15. Покупка тура при вводе невалидного значения месяца, меньше допустимого")
    void invalidZeroMonth() {
        var tourPage = new TourPage();
        var card = DataHelper.getFirstCardInfo().getCardNumber();
        var month = "0";
        var year = DataHelper.generateYear(0, "yy");
        var owner = DataHelper.generateOwner();
        var code = DataHelper.generateSecurityCode(3, true);
        DataHelper.OwnerCardInfo ownerCardInfo = new DataHelper.OwnerCardInfo(card, month, year, owner, code);
        tourPage.clickBuyButton().orderTourPayment(ownerCardInfo);
        var paymentPage = new PaymentPage();
        paymentPage.findMonthError();
    }

    @Test
    @DisplayName("Сценарий №16. Покупка тура при вводе русских букв в поле ввода месяца")
    void invalidRussianLetterMonth() {
        var tourPage = new TourPage();
        var card = DataHelper.getFirstCardInfo().getCardNumber();
        var month = "ФЧ";
        var year = DataHelper.generateYear(0, "yy");
        var owner = DataHelper.generateOwner();
        var code = DataHelper.generateSecurityCode(3, true);
        DataHelper.OwnerCardInfo ownerCardInfo = new DataHelper.OwnerCardInfo(card, month, year, owner, code);
        tourPage.clickBuyButton().orderTourPayment(ownerCardInfo);
        var paymentPage = new PaymentPage();
        paymentPage.findMonthError();
    }

    @Test
    @DisplayName("Сценарий №17. Покупка тура при вводе латинских букв в поле ввода месяца")
    void invalidLatinLetterMonth() {
        var tourPage = new TourPage();
        var card = DataHelper.getFirstCardInfo().getCardNumber();
        var month = "GL";
        var year = DataHelper.generateYear(0, "yy");
        var owner = DataHelper.generateOwner();
        var code = DataHelper.generateSecurityCode(3, true);
        DataHelper.OwnerCardInfo ownerCardInfo = new DataHelper.OwnerCardInfo(card, month, year, owner, code);
        tourPage.clickBuyButton().orderTourPayment(ownerCardInfo);
        var paymentPage = new PaymentPage();
        paymentPage.findMonthError();
    }

    @Test
    @DisplayName("Сценарий №18. Покупка тура при вводе спецсимволов в поле ввода месяца")
    void invalidSymbolMonth() {
        var tourPage = new TourPage();
        var card = DataHelper.getFirstCardInfo().getCardNumber();
        var month = "%&";
        var year = DataHelper.generateYear(0, "yy");
        var owner = DataHelper.generateOwner();
        var code = DataHelper.generateSecurityCode(3, true);
        DataHelper.OwnerCardInfo ownerCardInfo = new DataHelper.OwnerCardInfo(card, month, year, owner, code);
        tourPage.clickBuyButton().orderTourPayment(ownerCardInfo);
        var paymentPage = new PaymentPage();
        paymentPage.findMonthError();
    }

    @Test
    @DisplayName("Сценарий №19. Покупка тура при вводе дефиса в поле ввода месяца")
    void invalidDashMonth() {
        var tourPage = new TourPage();
        var card = DataHelper.getFirstCardInfo().getCardNumber();
        var month = "--";
        var year = DataHelper.generateYear(0, "yy");
        var owner = DataHelper.generateOwner();
        var code = DataHelper.generateSecurityCode(3, true);
        DataHelper.OwnerCardInfo ownerCardInfo = new DataHelper.OwnerCardInfo(card, month, year, owner, code);
        tourPage.clickBuyButton().orderTourPayment(ownerCardInfo);
        var paymentPage = new PaymentPage();
        paymentPage.findMonthError();
    }

    @Test
    @DisplayName("Сценарий №20. Покупка тура при вводе пробела в поле ввода месяца")
    void invalidSpaceMonth() {
        var tourPage = new TourPage();
        var card = DataHelper.getFirstCardInfo().getCardNumber();
        var month = "  ";
        var year = DataHelper.generateYear(0, "yy");
        var owner = DataHelper.generateOwner();
        var code = DataHelper.generateSecurityCode(3, true);
        DataHelper.OwnerCardInfo ownerCardInfo = new DataHelper.OwnerCardInfo(card, month, year, owner, code);
        tourPage.clickBuyButton().orderTourPayment(ownerCardInfo);
        var paymentPage = new PaymentPage();
        paymentPage.findMonthError();
    }

    @Test
    @DisplayName("Сценарий №21. Покупка тура при вводе года,меньше текущего")
    void invalidSmallerYear() {
        var tourPage = new TourPage();
        var card = DataHelper.getFirstCardInfo().getCardNumber();
        var month = DataHelper.generateValidMonth(0, "MM");
        var year = DataHelper.generateYear(-1, "yy");
        var owner = DataHelper.generateOwner();
        var code = DataHelper.generateSecurityCode(3, true);
        DataHelper.OwnerCardInfo ownerCardInfo = new DataHelper.OwnerCardInfo(card, month, year, owner, code);
        tourPage.clickBuyButton().orderTourPayment(ownerCardInfo);
        var paymentPage = new PaymentPage();
        paymentPage.findYearError();
    }

    @Test
    @DisplayName("Сценарий №22. Покупка тура при вводе года,больше допустимого значения")
    void invalidBiggerYear() {
        var tourPage = new TourPage();
        var card = DataHelper.getFirstCardInfo().getCardNumber();
        var month = DataHelper.generateValidMonth(0, "MM");
        var year = DataHelper.generateYear(6, "yy");
        var owner = DataHelper.generateOwner();
        var code = DataHelper.generateSecurityCode(3, true);
        DataHelper.OwnerCardInfo ownerCardInfo = new DataHelper.OwnerCardInfo(card, month, year, owner, code);
        tourPage.clickBuyButton().orderTourPayment(ownerCardInfo);
        var paymentPage = new PaymentPage();
        paymentPage.findYearError();
    }

    @Test
    @DisplayName("Сценарий №23. Покупка тура при вводе русских букв в поле ввода года")
    void invalidRussianLetterYear() {
        var tourPage = new TourPage();
        var card = DataHelper.getFirstCardInfo().getCardNumber();
        var month = DataHelper.generateValidMonth(0, "MM");
        var year = "ФН";
        var owner = DataHelper.generateOwner();
        var code = DataHelper.generateSecurityCode(3, true);
        DataHelper.OwnerCardInfo ownerCardInfo = new DataHelper.OwnerCardInfo(card, month, year, owner, code);
        tourPage.clickBuyButton().orderTourPayment(ownerCardInfo);
        var paymentPage = new PaymentPage();
        paymentPage.findYearError();
    }

    @Test
    @DisplayName("Сценарий №24. Покупка тура при вводе латинских букв в поле ввода года")
    void invalidLatinLetterYear() {
        var tourPage = new TourPage();
        var card = DataHelper.getFirstCardInfo().getCardNumber();
        var month = DataHelper.generateValidMonth(0, "MM");
        var year = "GF";
        var owner = DataHelper.generateOwner();
        var code = DataHelper.generateSecurityCode(3, true);
        DataHelper.OwnerCardInfo ownerCardInfo = new DataHelper.OwnerCardInfo(card, month, year, owner, code);
        tourPage.clickBuyButton().orderTourPayment(ownerCardInfo);
        var paymentPage = new PaymentPage();
        paymentPage.findYearError();
    }

    @Test
    @DisplayName("Сценарий №25. Покупка тура при вводе спецсимволов в поле ввода года")
    void invalidSymbolYear() {
        var tourPage = new TourPage();
        var card = DataHelper.getFirstCardInfo().getCardNumber();
        var month = DataHelper.generateValidMonth(0, "MM");
        var year = "%&";
        var owner = DataHelper.generateOwner();
        var code = DataHelper.generateSecurityCode(3, true);
        DataHelper.OwnerCardInfo ownerCardInfo = new DataHelper.OwnerCardInfo(card, month, year, owner, code);
        tourPage.clickBuyButton().orderTourPayment(ownerCardInfo);
        var paymentPage = new PaymentPage();
        paymentPage.findYearError();
    }

    @Test
    @DisplayName("Сценарий №26. Покупка тура при вводе дефиса в поле ввода года")
    void invalidDashYear() {
        var tourPage = new TourPage();
        var card = DataHelper.getFirstCardInfo().getCardNumber();
        var month = DataHelper.generateValidMonth(0, "MM");
        var year = "--";
        var owner = DataHelper.generateOwner();
        var code = DataHelper.generateSecurityCode(3, true);
        DataHelper.OwnerCardInfo ownerCardInfo = new DataHelper.OwnerCardInfo(card, month, year, owner, code);
        tourPage.clickBuyButton().orderTourPayment(ownerCardInfo);
        var paymentPage = new PaymentPage();
        paymentPage.findYearError();
    }

    @Test
    @DisplayName("Сценарий №27. Покупка тура при вводе пробела в поле ввода года")
    void invalidSpaceYear() {
        var tourPage = new TourPage();
        var card = DataHelper.getFirstCardInfo().getCardNumber();
        var month = DataHelper.generateValidMonth(0, "MM");
        var year = "  ";
        var owner = DataHelper.generateOwner();
        var code = DataHelper.generateSecurityCode(3, true);
        DataHelper.OwnerCardInfo ownerCardInfo = new DataHelper.OwnerCardInfo(card, month, year, owner, code);
        tourPage.clickBuyButton().orderTourPayment(ownerCardInfo);
        var paymentPage = new PaymentPage();
        paymentPage.findYearError();
    }

    @Test
    @DisplayName("Сценарий №28. Покупка тура при вводе одной буквы в поле Владелец")
    void invalidOneLetterName() {
        var tourPage = new TourPage();
        var card = DataHelper.getFirstCardInfo().getCardNumber();
        var month = DataHelper.generateValidMonth(0, "MM");
        var year = DataHelper.generateYear(0, "yy");
        var owner = DataHelper.generateInvalidOwner(1);
        var code = DataHelper.generateSecurityCode(3, true);
        DataHelper.OwnerCardInfo ownerCardInfo = new DataHelper.OwnerCardInfo(card, month, year, owner, code);
        tourPage.clickBuyButton().orderTourPayment(ownerCardInfo);
        var paymentPage = new PaymentPage();
        paymentPage.findOwnerError();
    }

    @Test
    @DisplayName("Сценарий №29. Покупка тура при вводе 65 букв в поле Владелец")
    void invalidLargeName() {
        var tourPage = new TourPage();
        var card = DataHelper.getFirstCardInfo().getCardNumber();
        var month = DataHelper.generateValidMonth(0, "MM");
        var year = DataHelper.generateYear(0, "yy");
        var owner = DataHelper.generateInvalidOwner(65);
        var code = DataHelper.generateSecurityCode(3, true);
        DataHelper.OwnerCardInfo ownerCardInfo = new DataHelper.OwnerCardInfo(card, month, year, owner, code);
        tourPage.clickBuyButton().orderTourPayment(ownerCardInfo);
        var paymentPage = new PaymentPage();
        paymentPage.findOwnerError();
    }

    @Test
    @DisplayName("Сценарий №30. Покупка тура при вводе русских букв в поле Владелец")
    void invalidRussianLetterName() {
        var tourPage = new TourPage();
        var card = DataHelper.getFirstCardInfo().getCardNumber();
        var month = DataHelper.generateValidMonth(0, "MM");
        var year = DataHelper.generateYear(0, "yy");
        var owner = DataHelper.generateRussianOwner();
        var code = DataHelper.generateSecurityCode(3, true);
        DataHelper.OwnerCardInfo ownerCardInfo = new DataHelper.OwnerCardInfo(card, month, year, owner, code);
        tourPage.clickBuyButton().orderTourPayment(ownerCardInfo);
        var paymentPage = new PaymentPage();
        paymentPage.findOwnerError();
    }

    @Test
    @DisplayName("Сценарий №31. Покупка тура при вводе дефиса перед именем в поле Владелец")
    void invalidDashBeforeName() {
        var tourPage = new TourPage();
        var card = DataHelper.getFirstCardInfo().getCardNumber();
        var month = DataHelper.generateValidMonth(0, "MM");
        var year = DataHelper.generateYear(0, "yy");
        var owner = "-" + DataHelper.generateOwner();
        var code = DataHelper.generateSecurityCode(3, true);
        DataHelper.OwnerCardInfo ownerCardInfo = new DataHelper.OwnerCardInfo(card, month, year, owner, code);
        tourPage.clickBuyButton().orderTourPayment(ownerCardInfo);
        var paymentPage = new PaymentPage();
        paymentPage.findOwnerError();
    }

    @Test
    @DisplayName("Сценарий №32. Покупка тура при вводе дефиса после имени в поле Владелец")
    void invalidDashAfterName() {
        var tourPage = new TourPage();
        var card = DataHelper.getFirstCardInfo().getCardNumber();
        var month = DataHelper.generateValidMonth(0, "MM");
        var year = DataHelper.generateYear(0, "yy");
        var owner = "Darya- Ivanova";
        var code = DataHelper.generateSecurityCode(3, true);
        DataHelper.OwnerCardInfo ownerCardInfo = new DataHelper.OwnerCardInfo(card, month, year, owner, code);
        tourPage.clickBuyButton().orderTourPayment(ownerCardInfo);
        var paymentPage = new PaymentPage();
        paymentPage.findOwnerError();
    }

    @Test
    @DisplayName("Сценарий №33. Покупка тура при вводе дефиса перед фамилией в поле Владелец")
    void invalidDashBeforeLastname() {
        var tourPage = new TourPage();
        var card = DataHelper.getFirstCardInfo().getCardNumber();
        var month = DataHelper.generateValidMonth(0, "MM");
        var year = DataHelper.generateYear(0, "yy");
        var owner = "Darya -Ivanova";
        var code = DataHelper.generateSecurityCode(3, true);
        DataHelper.OwnerCardInfo ownerCardInfo = new DataHelper.OwnerCardInfo(card, month, year, owner, code);
        tourPage.clickBuyButton().orderTourPayment(ownerCardInfo);
        var paymentPage = new PaymentPage();
        paymentPage.findOwnerError();
    }

    @Test
    @DisplayName("Сценарий №34. Покупка тура при вводе дефиса после фамилии в поле Владелец")
    void invalidDashAfterLastname() {
        var tourPage = new TourPage();
        var card = DataHelper.getFirstCardInfo().getCardNumber();
        var month = DataHelper.generateValidMonth(0, "MM");
        var year = DataHelper.generateYear(0, "yy");
        var owner = DataHelper.generateOwner() + "-";
        var code = DataHelper.generateSecurityCode(3, true);
        DataHelper.OwnerCardInfo ownerCardInfo = new DataHelper.OwnerCardInfo(card, month, year, owner, code);
        tourPage.clickBuyButton().orderTourPayment(ownerCardInfo);
        var paymentPage = new PaymentPage();
        paymentPage.findOwnerError();
    }

    @Test
    @DisplayName("Сценарий №35. Покупка тура при вводе цифр в поле Владелец")
    void invalidNumberName() {
        var tourPage = new TourPage();
        var card = DataHelper.getFirstCardInfo().getCardNumber();
        var month = DataHelper.generateValidMonth(0, "MM");
        var year = DataHelper.generateYear(0, "yy");
        var owner = DataHelper.generateOwner() + DataHelper.generateSecurityCode(3, true);
        var code = DataHelper.generateSecurityCode(3, true);
        DataHelper.OwnerCardInfo ownerCardInfo = new DataHelper.OwnerCardInfo(card, month, year, owner, code);
        tourPage.clickBuyButton().orderTourPayment(ownerCardInfo);
        var paymentPage = new PaymentPage();
        paymentPage.findOwnerError();
    }

    @Test
    @DisplayName("Сценарий №36. Покупка тура при вводе спецсимволов в поле Владелец")
    void invalidSymbolName() {
        var tourPage = new TourPage();
        var card = DataHelper.getFirstCardInfo().getCardNumber();
        var month = DataHelper.generateValidMonth(0, "MM");
        var year = DataHelper.generateYear(0, "yy");
        var owner = "%^&*";
        var code = DataHelper.generateSecurityCode(3, true);
        DataHelper.OwnerCardInfo ownerCardInfo = new DataHelper.OwnerCardInfo(card, month, year, owner, code);
        tourPage.clickBuyButton().orderTourPayment(ownerCardInfo);
        var paymentPage = new PaymentPage();
        paymentPage.findOwnerError();
    }

    @Test
    @DisplayName("Сценарий №37. Покупка тура при вводе пробела в поле Владелец")
    void invalidSpaceName() {
        var tourPage = new TourPage();
        var card = DataHelper.getFirstCardInfo().getCardNumber();
        var month = DataHelper.generateValidMonth(0, "MM");
        var year = DataHelper.generateYear(0, "yy");
        var owner = "  ";
        var code = DataHelper.generateSecurityCode(3, true);
        DataHelper.OwnerCardInfo ownerCardInfo = new DataHelper.OwnerCardInfo(card, month, year, owner, code);
        tourPage.clickBuyButton().orderTourPayment(ownerCardInfo);
        var paymentPage = new PaymentPage();
        paymentPage.findOwnerError();
    }

    @Test
    @DisplayName("Сценарий №38. Покупка тура при вводе двух цифр в поле CVC/CVV")
    void invalidCode() {
        var tourPage = new TourPage();
        var card = DataHelper.getFirstCardInfo().getCardNumber();
        var month = DataHelper.generateValidMonth(0, "MM");
        var year = DataHelper.generateYear(0, "yy");
        var owner = DataHelper.generateOwner();
        var code = DataHelper.generateSecurityCode(2, true);
        DataHelper.OwnerCardInfo ownerCardInfo = new DataHelper.OwnerCardInfo(card, month, year, owner, code);
        tourPage.clickBuyButton().orderTourPayment(ownerCardInfo);
        var paymentPage = new PaymentPage();
        paymentPage.findCardCodeError();
    }

    @Test
    @DisplayName("Сценарий №39. Покупка тура при вводе четырех цифр в поле CVC/CVV")
    void invalidBiggerCode() {
        var tourPage = new TourPage();
        var card = DataHelper.getFirstCardInfo().getCardNumber();
        var month = DataHelper.generateValidMonth(0, "MM");
        var year = DataHelper.generateYear(0, "yy");
        var owner = DataHelper.generateOwner();
        var code = DataHelper.generateSecurityCode(4, true);
        DataHelper.OwnerCardInfo ownerCardInfo = new DataHelper.OwnerCardInfo(card, month, year, owner, code);
        tourPage.clickBuyButton().orderTourPayment(ownerCardInfo);
        var paymentPage = new PaymentPage();
        paymentPage.findCardCodeError();
    }

    @Test
    @DisplayName("Сценарий №40. Покупка тура при вводе спецсимволов в поле CVC/CVV")
    void invalidSymbolCode() {
        var tourPage = new TourPage();
        var card = DataHelper.getFirstCardInfo().getCardNumber();
        var month = DataHelper.generateValidMonth(0, "MM");
        var year = DataHelper.generateYear(0, "yy");
        var owner = DataHelper.generateOwner();
        var code = "%^&";
        DataHelper.OwnerCardInfo ownerCardInfo = new DataHelper.OwnerCardInfo(card, month, year, owner, code);
        tourPage.clickBuyButton().orderTourPayment(ownerCardInfo);
        var paymentPage = new PaymentPage();
        paymentPage.findCardCodeError();
    }

    @Test
    @DisplayName("Сценарий №41. Покупка тура при вводе дефиса в поле CVC/CVV")
    void invalidDashCode() {
        var tourPage = new TourPage();
        var card = DataHelper.getFirstCardInfo().getCardNumber();
        var month = DataHelper.generateValidMonth(0, "MM");
        var year = DataHelper.generateYear(0, "yy");
        var owner = DataHelper.generateOwner();
        var code = "---";
        DataHelper.OwnerCardInfo ownerCardInfo = new DataHelper.OwnerCardInfo(card, month, year, owner, code);
        tourPage.clickBuyButton().orderTourPayment(ownerCardInfo);
        var paymentPage = new PaymentPage();
        paymentPage.findCardCodeError();
    }

    @Test
    @DisplayName("Сценарий №42. Покупка тура при пробела в поле CVC/CVV")
    void invalidSpaceCode() {
        var tourPage = new TourPage();
        var card = DataHelper.getFirstCardInfo().getCardNumber();
        var month = DataHelper.generateValidMonth(0, "MM");
        var year = DataHelper.generateYear(0, "yy");
        var owner = DataHelper.generateOwner();
        var code = "   ";
        DataHelper.OwnerCardInfo ownerCardInfo = new DataHelper.OwnerCardInfo(card, month, year, owner, code);
        tourPage.clickBuyButton().orderTourPayment(ownerCardInfo);
        var paymentPage = new PaymentPage();
        paymentPage.findCardCodeError();
    }

    @Test
    @DisplayName("Сценарий №43. Покупка тура при вводе латинских букв в поле CVC/CVV")
    void invalidLatinLetterCode() {
        var tourPage = new TourPage();
        var card = DataHelper.getFirstCardInfo().getCardNumber();
        var month = DataHelper.generateValidMonth(0, "MM");
        var year = DataHelper.generateYear(0, "yy");
        var owner = DataHelper.generateOwner();
        var code = DataHelper.generateInvalidOwner(3);
        DataHelper.OwnerCardInfo ownerCardInfo = new DataHelper.OwnerCardInfo(card, month, year, owner, code);
        tourPage.clickBuyButton().orderTourPayment(ownerCardInfo);
        var paymentPage = new PaymentPage();
        paymentPage.findCardCodeError();
    }

    @Test
    @DisplayName("Сценарий №44. Покупка тура при вводе русских букв в поле CVC/CVV")
    void invalidRussianLetterCode() {
        var tourPage = new TourPage();
        var card = DataHelper.getFirstCardInfo().getCardNumber();
        var month = DataHelper.generateValidMonth(0, "MM");
        var year = DataHelper.generateYear(0, "yy");
        var owner = DataHelper.generateOwner();
        var code = "ЯПУ";
        DataHelper.OwnerCardInfo ownerCardInfo = new DataHelper.OwnerCardInfo(card, month, year, owner, code);
        tourPage.clickBuyButton().orderTourPayment(ownerCardInfo);
        var paymentPage = new PaymentPage();
        paymentPage.findCardCodeError();
    }

    @Test
    @DisplayName("Сценарий №45. Покупка тура при вводе нулей в поле CVC/CVV")
    void invalidZeroCode() {
        var tourPage = new TourPage();
        var card = DataHelper.getFirstCardInfo().getCardNumber();
        var month = DataHelper.generateValidMonth(0, "MM");
        var year = DataHelper.generateYear(0, "yy");
        var owner = DataHelper.generateOwner();
        var code = "000";
        DataHelper.OwnerCardInfo ownerCardInfo = new DataHelper.OwnerCardInfo(card, month, year, owner, code);
        tourPage.clickBuyButton().orderTourPayment(ownerCardInfo);
        var paymentPage = new PaymentPage();
        paymentPage.findCardCodeError();
    }

}
