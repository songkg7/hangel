package org.discord.hansel;

import org.junit.jupiter.api.Test;

import java.net.URISyntaxException;

import static org.assertj.core.api.Assertions.assertThat;

class CoupangUrlParserTest {

    @Test
    void parseSnackName() throws URISyntaxException {
        String snackName = CoupangUrlParser.parseQuery("https://www.coupang.com/vp/products/188563628?itemId=17847244728&vendorItemId=85270354359&pickType=COU_PICK&q=%EC%BD%98%EC%B9%A9&itemsCount=36&searchId=e16b09e012ad48178477e44077b03835&rank=1&isAddedCart=");
        assertThat(snackName).isEqualTo("콘칩");
    }

}
