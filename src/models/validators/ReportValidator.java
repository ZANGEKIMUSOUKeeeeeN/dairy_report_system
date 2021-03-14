package models.validators;

import java.util.ArrayList;
import java.util.List;

import models.Report;

public class ReportValidator {
    public static List<String> validate(Report r){
        List<String> errors = new ArrayList<String>();

        String title_error = validateTitle(r.getTitle());
        if(!title_error.equals("")){
            errors.add(title_error);
        }

        String content_error = validateContent(r.getContent());
        if(!content_error.equals("")){
            errors.add(content_error);
        }
        return errors;

    }

    private static String validateTitle(String title){
        if(title == null || title.equals("")){
            return "タイトルを入力してください。";
        }
        return "";
    }

    private static String validateContent(String content){
        if(content == null || content.equals("")){
            return "内容を入力してください。";
        }
        return "";
    }

}
