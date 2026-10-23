package com.lsnju.base.util;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.ZonedDateTime;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.assertj.core.util.Lists;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.json.JsonMapper;
import com.lsnju.base.jackson.JacksonUtils;
import com.lsnju.base.money.Money;
import com.lsnju.base.util.enums.StatusEnum;
import com.lsnju.base.util.vo.DateTimeBean;
import com.lsnju.base.util.vo.TestBean;
import com.lsnju.base.util.vo.TestBean2;
import com.lsnju.base.util.vo.TestVo;

import lombok.extern.slf4j.Slf4j;

/**
 * @author lisong
 * @since 2020/3/18 19:38
 * @version V1.0
 */
@Slf4j
public class JacksonUtilsFunTest {

    @Test
    public void test_001() {
        final String memo = "{\"abc\":\"def\",\"key\":  \"value\"}";
        final TestBean obj = new TestBean();
        obj.setId(11);
        obj.setName("{\"name\":111}");
        obj.setMemo(memo);
        obj.setAmount(new Money("1.11"));
        final String js = JacksonUtils.toJsonPretty(obj);
        log.info("{}", js);
        final TestBean bean = JacksonUtils.fromJson(js, TestBean.class);
        log.info("{}", bean);
        Assertions.assertNotNull(bean);
        Assertions.assertEquals(memo, bean.getMemo());
    }

    @Test
    public void test_from_json() {
        final String memo = "{\"abc\":\"def\",\"key\":  \"value\"}";
        final TestBean obj = new TestBean();
        obj.setId(11);
        obj.setName("{\"name\":111}");
        obj.setMemo(memo);
        obj.setAmount(new Money("1.11"));
        final String js = JacksonUtils.toJsonPretty(Lists.newArrayList(obj));
        log.info("{}", js);
        final List<TestBean> list = JacksonUtils.fromJson(js, new TypeReference<>() {});
        log.info("{}", list);
        Assertions.assertNotNull(list);
        Assertions.assertEquals(1, list.size(), "xx");
    }

    @Test
    public void test_002() {
        Map<String, Object> map = new HashMap<>();
        map.put("abc", "def");
        map.put("key", "value");
        map.put("date", new Date());
        log.info("{}", JacksonUtils.toJson(map));
        log.info("{}", JacksonUtils.toJson(new Object()));
        log.info("{}", JacksonUtils.toJson(new TestVo()));
    }

    @Test
    void test_get_raw() throws IOException {
        final String json =
            "{\"alipay_data_dataservice_bill_downloadurl_query_response\":{\"code\":  \"10000\",\"age\": 111.1,\"newer\": null,\"msg\":\"Success\",\"list\":[\"aaa\",  \"bbb\"],\"bill_download_url\":\"http:\\/\\/dwbillcenter.alipay.com\\/downloadBillFile.resource?bizType=trade&userId=20886216406590780156&fileType=csv.zip&bizDates=20201224&downloadFileName=20886216406590780156_20201224.csv.zip&fileId=%2Ftrade%2F20886216406590780156%2F20201224.csv.zip&timestamp=1608959285&token=f5fbe03e29286bd0121d2cd1d8a8f3be\"},\"sign\":\"VTbFLAwGg3JJwGZxwa3B+uaZQDeK2HUY3/gCVu5a3xsmrEUTms/zkUj2Ehy5ONrAepsjIdpXuhfiRzL2GutCuxMhGnx+AQypWhGHh6tHg2JSiQt7vf/d1F82EPEvOfzzLap/yvrrjPFB+EVu50vJNCD42kxs82QyLdJ2oNI3F/f2Lq2nAAD8kdUa5erAKQO9nI2mkIO08UsPSfq7U0fa2YaMofiipkwh9p5KmrzExapGyacxukIUt2MHpM/qFEa2oLMqG3i5JwUy0ZD9chhbOHf8yw3nvhEZN5sFXcWUDPmeEVTH1ZQokb+jxWQgpNjzsXKFyTHZy2/9kOKcBXCPlg==\"}";
        final String tag = "alipay_data_dataservice_bill_downloadurl_query_response";
        log.info("{}", JacksonUtils.getRawValue(json, new String[]{tag, "newer"}) == null);
        log.info("{}", JacksonUtils.getRawValue(json, new String[]{tag, "age"}));
        log.info("{}", JacksonUtils.getRawValue(json, new String[]{tag, "list"}));
        log.info("{}", JacksonUtils.getRawValue(json, new String[]{tag, "msg"}));
        log.info("{}", JacksonUtils.getRawValue(json, new String[]{tag}));
        log.info("{}", JacksonUtils.getRawValue(json, new String[]{"sign"}));
    }


    @Test
    public void test_003() {
        final TestBean obj = new TestBean();
        obj.setId(11);
        obj.setName("{\"name\":111}");
        obj.setAmount(new Money("1.11"));
        final String js = JacksonUtils.toJsonPretty(obj);
        log.info("{}", js);
        final TestBean bean = JacksonUtils.fromJson(js, TestBean.class);
        log.info("{}", bean);
        Assertions.assertNotNull(bean);
    }

    @Test
    void test_jackson() {
        try {
            String json = "{\"date\":\"2022-09-01 14:47:32\",\"abc\":\"def\",\"blank\":\"\"}";
            JsonMapper mapper = new JsonMapper();

            final Map<String, String> value = mapper.readValue(json, new TypeReference<>() {});
            log.info("{}", value);

            JsonNode root = mapper.readTree(json);
            log.info("{}", mapper.convertValue(root, new TypeReference<Map<String, String>>() {}));

        } catch (Exception e) {
            log.error(String.format("%s", e.getMessage()), e);
        }
    }

    @Test
    void test_dateTime() throws IOException {
        DateTimeBean b = new DateTimeBean();
        b.setName("name");
        b.setZonedDateTime(ZonedDateTime.now());
        b.setLocalDateTime(LocalDateTime.now());
        b.setDate(new Date());
        final String jsonStr = JacksonUtils.toJsonPretty(b);
        log.info("{}", jsonStr);
        final DateTimeBean ret = JacksonUtils.fromJson(jsonStr, DateTimeBean.class);
        log.info("{}", ret);

        log.info("{}", TpJsonUtils.jackson().toJsonPretty(b));
        log.info("{}", TpJsonUtils.gson().toJsonPretty(b));
    }

    @Test
    void test_money_to_decimal() {
        final TestBean obj = new TestBean();
        obj.setId(11);
        obj.setName("xxx");
        obj.setAmount(new Money("1.11111"));
        obj.setStatus(StatusEnum.E);
        final String js = JacksonUtils.toJsonPretty(obj);
        log.info("{}", js);
        TestBean2 bean2 = JacksonUtils.fromJson(js, TestBean2.class);
        log.info("{}", bean2);
        log.info("{}", JacksonUtils.toJsonPretty(bean2));
    }

}
