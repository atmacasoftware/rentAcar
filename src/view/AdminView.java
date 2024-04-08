package view;

import business.BookManager;
import business.BrandManager;
import business.CarManager;
import business.ModelManager;
import core.ComboItem;
import core.Helper;
import entity.*;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.text.MaskFormatter;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.text.ParseException;
import java.util.ArrayList;

public class AdminView extends Layout {
    private JPanel container;
    private JLabel lbl_loginUser;
    private JPanel pnl_top;
    private JTabbedPane pnl_brand;
    private JButton btn_exit;
    private JTable table_brand;
    private JTable table_model;
    private JComboBox cmd_brand_search;
    private JComboBox cmd_type_search;
    private JComboBox cmd_fuel_search;
    private JComboBox cmd_gear_search;
    private JLabel lbl_gear;
    private JButton btn_search;
    private JButton btn_clear;
    private JTable table_car;
    private JFormattedTextField fld_start_date;
    private JFormattedTextField fld_fnsh_date;
    private JComboBox cmd_gear_type;
    private JComboBox cmd_fuel_type;
    private JComboBox cmd_car_type;
    private JTable table_book;
    private JButton btn_find;
    private JButton btn_book_clear;
    private JTable table_book_list;
    private JButton btn_model_add;
    private JLabel lbl_model;
    private JTextField fld_model;
    private JLabel lbl_year;
    private JTextField fld_year;
    private User user;
    private DefaultTableModel brand_model;
    private BrandManager brandManager;
    private JPopupMenu brandPopMenu;
    private ModelManager modelManager;
    private JPopupMenu modelPopMenu;
    private Object[] columsName;
    private CarManager carManager;
    private JPopupMenu carPopMenu;
    private BookManager bookManager;
    private Book book;
    private JPopupMenu bookPopMenu;
    private Object[] carBookColum;

    public AdminView(User user) {
        bookManager = new BookManager();
        brandManager = new BrandManager();
        modelManager = new ModelManager();
        carManager = new CarManager();
        this.user = user;
        add(container);
        guiInitilaze(1200, 700, "Yönetim Paneli");

        if (user == null) {
            Helper.showMsg("notFound");
            dispose();
        }

        lbl_loginUser.setText(lbl_loginUser.getText() + " " + user.getUser_name());

        // Markalar
        brandTableLoadRefresh();
        popMenusBrand();

        // Modeller
        modelTableLoadRefreshList(null);
        popMenusModels();
        modelsFilter();

        // Araçlar
        carTableLoadRefresh();
        popMenusCar();

        // Müşteriler
        bookTableLoadRefresh(null);
        popMenusBook();
        booksFilter();

        bookListTableRefresh();


        btn_exit.addActionListener(e -> System.exit(0));


    }

    //---------------------------------METOTLAR-------------------------------------------

    public void modelsFilter() {
        cmd_fuel_search.setModel(new DefaultComboBoxModel(Model.Fuel.values()));
        cmd_fuel_search.setSelectedItem(null);
        cmd_gear_search.setModel(new DefaultComboBoxModel(Model.Gear.values()));
        cmd_gear_search.setSelectedItem(null);
        cmd_type_search.setModel(new DefaultComboBoxModel(Model.Type.values()));
        cmd_type_search.setSelectedItem(null);
        brandNameFilter();
    }

    private void brandNameFilter() {
        cmd_brand_search.removeAllItems();
        for (Brand brand : brandManager.findByAll()) {
            cmd_brand_search.addItem(new ComboItem(brand.getBrand_id(), brand.getBrand_name()));
        }
        cmd_brand_search.setSelectedItem(null);
    }

    public void bookListTableRefresh() {
        Object[] columns = {"Kiralama ID", "Araba ID", "Kiralama Adı", "ID No", "Telefon", "E-Posta", "Başlangıç Tarihi", "Bitiş Tarihi", "Fiyat", "Not", "Durum"};
        ArrayList<Object[]> bookList = bookManager.getForTable(columns.length, bookManager.findByAll());
        createTable(brand_model, table_book_list, columns, bookList);
    }

    public void brandTableLoadRefresh() {
        Object[] columns = {"ID", "Marka Adı"};
        ArrayList<Object[]> brandsList = brandManager.getForTable(columns.length);
        createTable(brand_model, table_brand, columns, brandsList);


        btn_clear.addActionListener(e -> {
            cmd_fuel_search.setSelectedItem(null);
            cmd_gear_search.setSelectedItem(null);
            cmd_type_search.setSelectedItem(null);
            brandNameFilter();
        });


        btn_find.addActionListener(e -> {
            ArrayList<Car> carList = carManager.searchBooking(fld_start_date.getText(), fld_fnsh_date.getText(),
                    (Model.Type) cmd_car_type.getSelectedItem(),
                    (Model.Gear) cmd_gear_type.getSelectedItem(),
                    (Model.Fuel) cmd_fuel_type.getSelectedItem());
            ArrayList<Object[]> carBookRow = carManager.getForTable(carBookColum.length, carList);
            bookTableLoadRefresh(carBookRow);
        });


        btn_book_clear.addActionListener(e -> {
            cmd_car_type.setSelectedItem(null);
            cmd_fuel_type.setSelectedItem(null);
            cmd_gear_type.setSelectedItem(null);
            fld_fnsh_date.setText("");
            fld_start_date.setText("");
            bookTableLoadRefresh(null);
        });
    }

    public void carTableLoadRefresh() {
        Object[] columns = {"ID", "Marka", "Model", "Plaka", "Renk", "KM", "Yıl", "Tip", "Yakıt", "Vites"};
        ArrayList<Object[]> carList = carManager.getForTable(columns.length, carManager.findByAll());
        createTable(brand_model, table_car, columns, carList);
    }

    public void bookTableLoadRefresh(ArrayList<Object[]> carList) {
        carBookColum = new Object[]{"ID", "Araba", "Ad", "Sayı", "E-Posta", "Başlangıç Tarihi", "Bitiş Tarihi", "Fiyat", "Not", "Durum"};
        createTable(brand_model, table_book, columsName, carList);
    }

    public void modelTableLoadRefresh() {
        Object[] columns = {"ID", "Marka Adı", "Model Adı", "Tip", "Yıl", "Yakıt", "Vites"};
        ArrayList<Object[]> modelList = modelManager.getForTable(columns.length, modelManager.findByAll());
        createTable(brand_model, table_model, columns, modelList);
    }

    public void modelTableLoadRefreshList(ArrayList<Object[]> modelList) {
        columsName = new Object[]{"ID", "Marka Adı", "Model Adı", "Tip", "Yıl", "Yakıt", "Vites"};

        if (modelList == null) {
            modelList = modelManager.getForTable(columsName.length, modelManager.findByAll());
        }

        createTable(brand_model, table_model, columsName, modelList);
    }

    public void popMenusBrand() {
        // Brand Tablosu için pop menüler
        this.brandPopMenu = new JPopupMenu();

        // Mouse seçim işlemleri
        tableMouseSelect(table_brand);

        brandPopMenu.add("Yeni Ekle").addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                BrandSaveView brandView = new BrandSaveView(null);

                brandView.addWindowListener(new WindowAdapter() {
                    @Override
                    public void windowClosed(WindowEvent e) {
                        brandTableLoadRefresh();
                        modelsFilter();

                    }
                });
            }
        });

        brandPopMenu.add("Güncelle").addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                BrandSaveView brandSaveView = new BrandSaveView(brandManager.getById(getSelectedRow(table_brand, 0)));

                brandSaveView.addWindowListener(new WindowAdapter() {
                    @Override
                    public void windowClosed(WindowEvent e) {
                        brandTableLoadRefresh(); // Tabloyu yeniden oluşturur
                        modelTableLoadRefreshList(null);
                        modelsFilter();
                    }
                });
            }
        });

        brandPopMenu.add("Sil").addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (JOptionPane.showConfirmDialog(null, "Marka silinecek !", "Onaylama", JOptionPane.YES_NO_OPTION) == 0) {
                    brandManager.delete(getSelectedRow(table_brand, 0));
                    Helper.showMsg("done");
                    brandTableLoadRefresh();
                    modelTableLoadRefreshList(null);
                    modelsFilter();
                } else {
                    Helper.showMsg("cancel");
                }
            }
        });

        table_brand.setComponentPopupMenu(brandPopMenu);
    }

    public void popMenusModels() {

        this.modelPopMenu = new JPopupMenu();

        tableMouseSelect(table_model);

        modelPopMenu.add("Yeni Ekle").addActionListener(e -> {
            ModelSaveView modelSaveView = new ModelSaveView(new Model());
            modelSaveView.addWindowListener(new WindowAdapter() {
                @Override
                public void windowClosed(WindowEvent e) {
                    modelTableLoadRefresh();
                }
            });
        });

        modelPopMenu.add("Güncelle").addActionListener(e -> {
            ModelSaveView modelSaveView = new ModelSaveView(modelManager.getById(getSelectedRow(table_model, 0)));
            modelSaveView.addWindowListener(new WindowAdapter() {
                @Override
                public void windowClosed(WindowEvent e) {
                    modelTableLoadRefresh();
                }
            });
        });

        modelPopMenu.add("Sil").addActionListener(e -> {
            if (JOptionPane.showConfirmDialog(null, "Model silinecek !", "Onaylama", JOptionPane.YES_NO_OPTION) == 0) {
                modelManager.deleted(getSelectedRow(table_model, 0));
                Helper.showMsg("done");
                modelTableLoadRefresh();
            } else {
                Helper.showMsg("done");
            }
        });

        btn_model_add.addActionListener(e -> {
            JTextField[] checkFieldList = {this.fld_model, this.fld_year};
            JComboBox[] checkComboList = {cmd_brand_search, cmd_type_search, cmd_fuel_search, cmd_gear_search};
            if (Helper.isFieldListEmpty(checkFieldList) || Helper.isComboListEmpty(checkComboList)) {
                Helper.showMsg("fill");
            } else {
                ComboItem selectBrand = (ComboItem) cmd_brand_search.getSelectedItem();
                int selectBrandId = selectBrand.getKey();
                String selectedFuel = this.cmd_fuel_search.getSelectedItem().toString();
                String selectedGear = this.cmd_gear_search.getSelectedItem().toString();
                String selectedType = this.cmd_type_search.getSelectedItem().toString();

                System.out.println();

                Model obj = new Model(selectBrandId, this.fld_model.getText(), this.fld_year.getText(), Model.Type.valueOf(selectedType), Model.Gear.valueOf(selectedGear), Model.Fuel.valueOf(selectedFuel));
                this.modelManager.save(obj);
                modelTableLoadRefresh();
            }
        });

        btn_search.addActionListener(e -> {
            ComboItem selectItem = (ComboItem) cmd_brand_search.getSelectedItem();
            int brandID = 0;
            if (selectItem != null) {
                brandID = selectItem.getKey();
            }
            ArrayList<Model> modelListTableSearch =
                    modelManager.filterTable(brandID,
                            (Model.Fuel) cmd_fuel_search.getSelectedItem(),
                            (Model.Gear) cmd_gear_search.getSelectedItem(),
                            (Model.Type) cmd_type_search.getSelectedItem()
                    );
            ArrayList<Object[]> modelListObject = modelManager.getForTable(columsName.length, modelListTableSearch);
            modelTableLoadRefreshList(modelListObject);
        });

        table_model.setComponentPopupMenu(modelPopMenu);
    }

    public void popMenusCar() {
        this.carPopMenu = new JPopupMenu();

        tableMouseSelect(table_car);

        carPopMenu.add("Yeni Ekle").addActionListener(e -> {
            CarView carView = new CarView(new Car());

            carView.addWindowListener(new WindowAdapter() {
                @Override
                public void windowClosed(WindowEvent e) {
                    brandTableLoadRefresh();
                    modelsFilter();
                    carTableLoadRefresh();

                }
            });
        });
        carPopMenu.add("Güncelle").addActionListener(e -> {
            CarView carView = new CarView(carManager.getById(getSelectedRow(table_car, 0)));

            carView.addWindowListener(new WindowAdapter() {
                @Override
                public void windowClosed(WindowEvent e) {
                    brandTableLoadRefresh();
                    modelTableLoadRefreshList(null);
                    modelsFilter();
                    carTableLoadRefresh();
                }
            });
        });
        carPopMenu.add("Sil").addActionListener(e -> {

            if (JOptionPane.showConfirmDialog(null, "Araba silinecek !", "Onaylama", JOptionPane.YES_NO_OPTION) == 0) {
                carManager.deleted(getSelectedRow(table_car, 0));
                Helper.showMsg("done");
                brandTableLoadRefresh();
                modelTableLoadRefreshList(null);
                modelsFilter();
                carTableLoadRefresh();
            } else {
                Helper.showMsg("done");
            }
        });
        table_car.setComponentPopupMenu(carPopMenu);
    }

    public void popMenusBook() {
        bookPopMenu = new JPopupMenu();
        bookPopMenu.add("Rezervasyon").addActionListener(e -> {
            int selectId = getSelectedRow(table_book, 0);
            BookView bookView = new BookView(carManager.getById(selectId), fld_start_date.getText(), fld_fnsh_date.getText());
            bookView.addWindowListener(new WindowAdapter() {
                @Override
                public void windowClosed(WindowEvent e) {
                    bookTableLoadRefresh(null);
                    booksFilter();
                }
            });
        });
        table_book.setComponentPopupMenu(bookPopMenu);
    }

    public void booksFilter() {
        cmd_car_type.setModel(new DefaultComboBoxModel(Model.Type.values()));
        cmd_car_type.setSelectedItem(null);
        cmd_fuel_type.setModel(new DefaultComboBoxModel(Model.Fuel.values()));
        cmd_fuel_type.setSelectedItem(null);
        cmd_gear_type.setModel(new DefaultComboBoxModel(Model.Gear.values()));
        cmd_gear_type.setSelectedItem(null);
    }

    private void createUIComponents() {
        try {
            fld_start_date = new JFormattedTextField(new MaskFormatter("##/##/####"));
            fld_fnsh_date = new JFormattedTextField(new MaskFormatter("##/##/####"));
            fld_start_date.setText("10/10/2023");
            fld_fnsh_date.setText("16/10/2023");

        } catch (ParseException e) {
            throw new RuntimeException(e);
        }
    }


}
