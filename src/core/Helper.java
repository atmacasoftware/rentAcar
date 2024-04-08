package core;

import javax.swing.*;
import java.awt.*;

public class Helper {

    // Tema secimi
    public static void setTheme(){

        for(UIManager.LookAndFeelInfo info : UIManager.getInstalledLookAndFeels()){
            if(info.getName().equals("Nimbus")){
                try {
                    UIManager.setLookAndFeel(info.getClassName());
                } catch (ClassNotFoundException e) {
                    throw new RuntimeException(e);
                } catch (InstantiationException e) {
                    throw new RuntimeException(e);
                } catch (IllegalAccessException e) {
                    throw new RuntimeException(e);
                } catch (UnsupportedLookAndFeelException e) {
                    throw new RuntimeException(e);
                }
            }
        }
    }

    public static boolean isFieldEmpty(JTextField field) {
        return field.getText().trim().isEmpty();
    }

    public static boolean isFieldListEmpty(JTextField[] fieldList) {
        for (JTextField field : fieldList) {
            if (isFieldEmpty(field)) return true;
        }

        return false;
    }

    public static boolean isComboEmpty(JComboBox comboBox) {
        if (comboBox.getSelectedItem() == null) return true;
        return false;
    }


    public static boolean isComboListEmpty(JComboBox[] comboList) {
        for (JComboBox combo : comboList) {
            if (isComboEmpty(combo)) return true;
        }

        return false;
    }

    // Mesaj kutusu
    public static void showMsg(String str) {
        optionPaneTR();
        String msg;
        String title;
        switch (str) {
            case "fill" -> {
                msg = "Lütfen tüm alanları doldurunuz !";
                title = "Hata!";
            }
            case "done" -> {
                msg = "İşlem Başarılı !";
                title = "Sonuç";
            }
            case "notFound" -> {
                msg = "Kayıt bulunamadı !";
                title = "Bulunamadı";
            }
            case "cancel" -> {
                msg = "İptal edildi !";
                title = "Sonuç";
            }
            case "notNull" -> {
                msg = "Boş olamaz !";
                title = "Sonuç";
            }
            case "error" -> {
                msg = "Hatalı işlem yaptınız !";
                title = "Hata!";
            }
            default -> {
                msg = str;
                title = "Mesaj";
            }
        }
        JOptionPane.showMessageDialog(null, msg, title, JOptionPane.INFORMATION_MESSAGE);
    }

    public static int getLocationPoint(String type, Dimension size) {
        return switch (type) {
            case "x" -> (int) ((Toolkit.getDefaultToolkit().getScreenSize().width - size.getWidth()) / 2);
            case "y" -> (int) ((Toolkit.getDefaultToolkit().getScreenSize().height - size.getHeight()) / 2);
            default -> 0;
        };
    }

    public static void optionPaneTR(){
        UIManager.put("OptionPane.okButtonText", "Tamam");
        UIManager.put("OptionPane.yesButtonText", "Evet");
        UIManager.put("OptionPane.noButtonText", "Hayır");
    }


}