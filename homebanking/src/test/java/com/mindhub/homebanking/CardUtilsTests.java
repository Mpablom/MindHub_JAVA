package com.mindhub.homebanking;

import com.mindhub.homebanking.utils.CardsUtils;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

@SpringBootTest
public class CardUtilsTests {
   /* @Test
    public void cardNumberIsCreated(){
        String cardNumber = CardsUtils.generateCardNumber();
        assertThat(cardNumber,is(not(emptyOrNullString())));
    }

    @Test
    public void generateCvv(){
        int cvv = CardsUtils.generateCvv();
        assertThat(cvv, greaterThanOrEqualTo(100));
        assertThat(cvv, lessThanOrEqualTo(999));
    }*/
}
