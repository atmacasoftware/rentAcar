package view;

import business.BrandManager;
import core.Helper;
import entity.Brand;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class BrandSaveView extends Layout{
    private JPanel container;
    private JLabel lbl_brand;
    private JLabel lbl_branName;
    private JTextField fld_brandname;
    private JButton btn_save;
    private JButton btn_cancel;
    private BrandManager brandManager;
    private Brand brand;

    public BrandSaveView(Brand brand) {
        if(brand != null){
            fld_brandname.setText(brand.getBrand_name());
        }
        this.brand = brand;
        brandManager = new BrandManager();
        add(container);
        guiInitilaze(400,500,"Brand Edit");


        btn_cancel.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dispose();
            }
        });

        btn_save.addActionListener(e -> {
            if(Helper.isFieldEmpty(fld_brandname)){ // Boş ise
                Helper.showMsg("notNull");
            }else{
                boolean isSuccsess;

                if(brand == null){
                    isSuccsess = brandManager.save(new Brand(fld_brandname.getText()));
                }else {
                    brand.setBrand_name(fld_brandname.getText());
                    isSuccsess = brandManager.update(brand);
                }
                if(isSuccsess){
                    Helper.showMsg("done");
                    dispose();
                }else{
                    Helper.showMsg("error");
                }
            }
        });

    }
}
