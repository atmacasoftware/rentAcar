package business;

import core.Helper;
import dao.BrandDao;
import entity.Brand;
import entity.Model;

import java.util.ArrayList;

public class BrandManager {
    private BrandDao brandDao;
    private ModelManager modelManager;
    public BrandManager() {
        brandDao = new BrandDao();
        modelManager = new ModelManager();
    }

    public ArrayList<Object[]> getForTable(int size){
        ArrayList<Object[]> rowList = new ArrayList<>();
        for(Brand brand : findByAll()){ // Tablodaki veriler kadar döner
            Object[] rows = new Object[size];
            int i = 0;
            rows[i++] = brand.getBrand_id(); // Tablo başlığı 1' den başlar
            rows[i++] = brand.getBrand_name();
            rowList.add(rows);
        }
        return rowList;
    }
    public ArrayList<Brand> findByAll(){
        return brandDao.findByAll();
    }
    public boolean save(Brand brand){
        if(brand.getBrand_id() != 0){
            Helper.showMsg("Error");
        }
        return brandDao.save(brand);
    }
    public Brand getById(int id){
        return brandDao.getById(id);
    }
    public boolean update(Brand brand){
        if(getById(brand.getBrand_id()) == null){
            Helper.showMsg("Not found");
        }
        return brandDao.update(brand);
    }
    public boolean delete(int id){
        if(getById(id) == null){
            Helper.showMsg(id + "numaralı marka bulunamadı.");
            return false;
        }
        for(Model model : modelManager.getByBrandId(id)){
            modelManager.deleted(model.getModel_id());
        }
        return brandDao.delete(id);
    }
}