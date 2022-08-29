package ru.netology;

import com.codeborne.selenide.Configuration;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static com.codeborne.selenide.Condition.*;
import static com.codeborne.selenide.Selenide.*;

public class CardOrderServiceTest {

    @BeforeEach
    void setUp() {
        Configuration.holdBrowserOpen = true;
        open("http://localhost:9999/");
    }

    @AfterEach
    void memoryClear() {
        clearBrowserCookies();
        clearBrowserLocalStorage();
    }

    @Test
    void shouldTestNameMustContainRussianLetters() {
        $("input[name='name']").setValue("Иванов Иван");
        $("input[type='tel']").setValue("+79999999999");
        $("[data-test-id=agreement]").click();
        $x("//button").click();
        $("[data-test-id=order-success]").shouldHave(exactText("Ваша заявка успешно отправлена! Наш менеджер свяжется с вами в ближайшее время."));
    }

    @Test
    void shouldTestNameMustContainADash() {
        $("input[name='name']").setValue("Иванов-Петров Иван");
        $("input[type='tel']").setValue("+79999999999");
        $("[data-test-id=agreement]").click();
        $x("//button").click();
        $("[data-test-id=order-success]").shouldHave(exactText("Ваша заявка успешно отправлена! Наш менеджер свяжется с вами в ближайшее время."));
    }

    @Test
    void shouldTestEmptyName() {
        $("input[name='name']").setValue("");
        $("input[type='tel']").setValue("+79999999999");
        $("[data-test-id=agreement]").click();
        $x("//button").click();
        $("[data-test-id=name].input_invalid span.input__sub").shouldHave(exactText("Поле обязательно для заполнения"));
    }

    @Test
    void shouldTestLatinNames() {
        $("input[name='name']").setValue("Ivanov Ivan");
        $("input[type='tel']").setValue("+79999999999");
        $("[data-test-id=agreement]").click();
        $x("//button").click();
        $("[data-test-id=name].input_invalid span.input__sub").shouldHave(exactText("Имя и Фамилия указаные неверно. Допустимы только русские буквы, пробелы и дефисы."));
    }

    @Test
    void shouldTestNoSurname() {
        $("input[name='name']").setValue("Иван");
        $("input[type='tel']").setValue("+79999999999");
        $("[data-test-id=agreement]").click();
        $x("//button").click();
        $("[data-test-id=order-success]").shouldHave(exactText("Ваша заявка успешно отправлена! Наш менеджер свяжется с вами в ближайшее время."));
    }

    @Test
    void shouldTestNameWithNumbers() {
        $("input[name='name']").setValue("Иванов Иван2");
        $("input[type='tel']").setValue("+79999999999");
        $("[data-test-id=agreement]").click();
        $x("//button").click();
        $("[data-test-id=name].input_invalid span.input__sub").shouldHave(exactText("Имя и Фамилия указаные неверно. Допустимы только русские буквы, пробелы и дефисы."));
    }

    @Test
    void shouldTestNameWithSymbols() {
        $("input[name='name']").setValue("*Иванов Иван*");
        $("input[type='tel']").setValue("+79999999999");
        $("[data-test-id=agreement]").click();
        $x("//button").click();
        $("[data-test-id=name].input_invalid span.input__sub").shouldHave(exactText("Имя и Фамилия указаные неверно. Допустимы только русские буквы, пробелы и дефисы."));
    }

    @Test
    void shouldTestEmptyTel() {
        $("input[name='name']").setValue("Иванов Иван");
        $("input[type='tel']").setValue("");
        $("[data-test-id=agreement]").click();
        $x("//button").click();
        $("[data-test-id=phone].input_invalid span.input__sub").shouldHave(exactText("Поле обязательно для заполнения"));
    }

    @Test
    void shouldTestLessThan11Numbers() {
        $("input[name='name']").setValue("Иванов Иван");
        $("input[type='tel']").setValue("+1234567890");
        $("[data-test-id=agreement]").click();
        $x("//button").click();
        $("[data-test-id=phone].input_invalid span.input__sub").shouldHave(exactText("Телефон указан неверно. Должно быть 11 цифр, например, +79012345678."));
    }

    @Test
    void shouldTestTelStartsFrom8() {
        $("input[name='name']").setValue("Иванов Иван");
        $("input[type='tel']").setValue("88005553535"); //проще позвонить, чем у кого-то занимать))
        $("[data-test-id=agreement]").click();
        $x("//button").click();
        $("[data-test-id=phone].input_invalid span.input__sub").shouldHave(exactText("Телефон указан неверно. Должно быть 11 цифр, например, +79012345678."));
    }

    @Test
    void shouldTestTelWithSymbols() {
        $("input[name='name']").setValue("Иванов Иван");
        $("input[type='tel']").setValue("+7-999 999*99.99");
        $("[data-test-id=agreement]").click();
        $x("//button").click();
        $("[data-test-id=phone].input_invalid span.input__sub").shouldHave(exactText("Телефон указан неверно. Должно быть 11 цифр, например, +79012345678."));
    }

    @Test
    void shouldTestTelWithPlusAtTheEnd() {
        $("input[name='name']").setValue("Иванов Иван");
        $("input[type='tel']").setValue("79999999999+");
        $("[data-test-id=agreement]").click();
        $x("//button").click();
        $("[data-test-id=phone].input_invalid span.input__sub").shouldHave(exactText("Телефон указан неверно. Должно быть 11 цифр, например, +79012345678."));
    }

    @Test
    void shouldTestAgreementBoxUnchecked() {
        $("input[name='name']").setValue("Иванов Иван");
        $("input[type='tel']").setValue("+79999999999");
        $("[data-test-id=agreement]").doubleClick();
        $x("//button").click();
        $("label.input_invalid").shouldBe(visible);
    }
}