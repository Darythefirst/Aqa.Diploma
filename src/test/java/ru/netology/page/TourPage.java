package ru.netology.page;

import com.codeborne.selenide.SelenideElement;
import com.codeborne.selenide.selector.ByText;

import java.time.Duration;

import static com.codeborne.selenide.Condition.*;
import static com.codeborne.selenide.Selectors.byText;
import static com.codeborne.selenide.Selenide.$;

public class TourPage {
    private SelenideElement tourPage = $(byText("Путешествие дня"));
    private SelenideElement buyButton = $(byText("Купить"));
    private SelenideElement creditButton = $(byText("Купить в кредит"));

    public TourPage() {
        tourPage.shouldBe(visible, Duration.ofSeconds(15));
        buyButton.shouldBe(visible, Duration.ofSeconds(15));
        creditButton.shouldBe(visible, Duration.ofSeconds(15));
    }

    public PaymentPage clickBuyButton() {
        buyButton.click();
        return new PaymentPage();
    }

    public CreditPage clickCreditButton() {
        creditButton.click();
        return new CreditPage();
    }

}
