package ru.netology.page;

import com.codeborne.selenide.SelenideElement;
import ru.netology.data.DataHelper;

import java.time.Duration;

import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selectors.byText;
import static com.codeborne.selenide.Selectors.withText;
import static com.codeborne.selenide.Selenide.$;

public class PaymentPage {
    private SelenideElement payment = $(byText("Оплата по карте"));
    private SelenideElement card = $("input[placeholder='0000 0000 0000 0000']");
    private SelenideElement month = $(byText("Месяц")).parent().$("input");
    private SelenideElement year = $(byText("Год")).parent().$("input");
    private SelenideElement cardOwner = $(byText("Владелец")).parent().$("input");
    private SelenideElement cardCode = $("input[placeholder='999']");
    private SelenideElement contButton = $(byText("Продолжить"));
    private SelenideElement cardError = $(byText("Номер карты")).parent().$(".input__sub");
    private SelenideElement monthError = $(withText("Месяц")).parent().$(".input__sub");
    private SelenideElement yearError = $(withText("Год")).parent().$(".input__sub");
    private SelenideElement cardOwnerError = $(withText("Владелец")).parent().$(".input__sub");
    private SelenideElement cardCodeError = $(withText("CVC/CVV")).parent().$(".input__sub");
    private SelenideElement notification = $(".notification_status_ok");
    private SelenideElement notificationError = $(".notification_status_error");

    public PaymentPage() {
        payment.shouldBe(visible, Duration.ofSeconds(15));
    }

    public void orderTourPayment(DataHelper.OwnerCardInfo info) {
        card.setValue(info.getCard());
        month.setValue(info.getMonth());
        year.setValue(info.getYear());
        cardOwner.setValue(info.getOwner());
        cardCode.setValue(info.getSecurityCode());
        contButton.click();
    }

    public void findNotification() {
        notification.shouldBe(visible, Duration.ofSeconds(15));
    }

    public void findNotificationError() {
        notificationError.shouldBe(visible, Duration.ofSeconds(15));
    }

    public void findCardError() {
        cardError.shouldBe(visible, Duration.ofSeconds(15));
    }

    public void findMonthError() {
        monthError.shouldBe(visible, Duration.ofSeconds(15));
    }

    public void findYearError() {
        yearError.shouldBe(visible, Duration.ofSeconds(15));
    }

    public void findOwnerError() {
        cardOwnerError.shouldBe(visible, Duration.ofSeconds(15));
    }

    public void findCardCodeError() {
        cardCodeError.shouldBe(visible, Duration.ofSeconds(15));
    }
}
