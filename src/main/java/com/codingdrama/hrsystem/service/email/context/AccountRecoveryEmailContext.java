package com.codingdrama.hrsystem.service.email.context;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class AccountRecoveryEmailContext extends AbstractEmailContext {

    private List<String> passPhrases;


    @Override
    public <T> void init(T context){
        String email = (String) context;
        setTemplateLocation("emails/account-recovery");
        setSubject("BDACS Account 2FA Recovery");
        setFrom("alona.honcharenko@nure.ua");
        setTo(email);
    }

    public void setPassPhrases(List<String> passPhrases) {
        this.passPhrases = passPhrases;
        Map<Integer, String> map = IntStream.range(0, passPhrases.size())
                                           .boxed()
                                           .collect(Collectors.toMap(i -> i+1, passPhrases::get));
        put("recoveryPhrases",map);
    }

    public static String createHtmlTable(Map<Integer, String> data) {
        StringBuilder html = new StringBuilder();

        for (Integer key : data.keySet()) {
            html.append("<th>").append(key).append("</th>");
        }

        for (Integer key : data.keySet()) {
            String value = data.get(key);
            html.append("<tr>");
            html.append("<td>").append(key).append("</td>");
            html.append("<td>").append(value).append("</td>");
        }

        return html.toString();
    }
}
