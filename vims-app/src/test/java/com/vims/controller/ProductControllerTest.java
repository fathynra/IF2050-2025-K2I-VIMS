package com.vims.controller; 

import com.vims.dao.InvestmentProductDao;
import com.vims.model.InvestmentProduct;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class MockProductDaoForProductControllerTest extends InvestmentProductDao {
    private List<InvestmentProduct> mockProductList = new ArrayList<>();
    private List<InvestmentProduct> addedProducts = new ArrayList<>(); 

    public MockProductDaoForProductControllerTest(List<InvestmentProduct> initialProducts) {
        if (initialProducts != null) {
            this.mockProductList.addAll(initialProducts);
        }
    }

    @Override
    public List<InvestmentProduct> getAllInvestmentProducts() {
        return new ArrayList<>(mockProductList);
    }

    @Override
    public InvestmentProduct getInvestmentProductById(int productId) {
        return mockProductList.stream()
                .filter(p -> p.getProductId() == productId)
                .findFirst()
                .orElse(null);
    }

    @Override
    public boolean addInvestmentProduct(InvestmentProduct product) {
        
        if (product.getProductId() == 0) { 
            product.setProductId(mockProductList.size() + addedProducts.size() + 100); 
        }
        addedProducts.add(product); 
        mockProductList.add(product); 
        return true; 
    }
    
    public void clearAddedProducts() {
        mockProductList.removeAll(addedProducts);
        addedProducts.clear();
    }
}

public class ProductControllerTest {

    private ProductController productController;
    private MockProductDaoForProductControllerTest mockProductDao;
    private List<InvestmentProduct> sampleProducts;

    @BeforeEach
    void setUp() {
        sampleProducts = new ArrayList<>(Arrays.asList(
                new InvestmentProduct(1, "Saham Alpha", "stock", "Tinggi", "Deskripsi Alpha", new BigDecimal("100.00")),
                new InvestmentProduct(2, "Obligasi Beta", "bond", "Rendah", "Deskripsi Beta", new BigDecimal("1000.00")),
                new InvestmentProduct(3, "Saham Gamma", "stock", "Sedang", "Deskripsi Gamma", new BigDecimal("50.00")),
                new InvestmentProduct(4, "Properti Delta", "real estate", "Sedang", "Deskripsi Delta", new BigDecimal("50000.00"))
        ));
        mockProductDao = new MockProductDaoForProductControllerTest(sampleProducts);
        productController = new ProductController(mockProductDao); 
    }

    @Test
    @DisplayName("Pencarian berdasarkan nama produk yang ada sebagian")
    void searchProductsByName_shouldReturnMatchingProducts_whenPartialNameExists() {
        List<InvestmentProduct> results = productController.searchProductsByName("Alpha");
        assertNotNull(results);
        assertEquals(1, results.size());
        assertEquals("Saham Alpha", results.get(0).getName());
    }

    @Test
    @DisplayName("Pencarian dengan string kosong seharusnya mengembalikan semua produk")
    void searchProductsByName_shouldReturnAllProducts_whenSearchTextIsEmpty() {
        List<InvestmentProduct> results = productController.searchProductsByName("");
        assertNotNull(results);
        assertEquals(sampleProducts.size(), results.size());
    }
    
    @Test
    @DisplayName("addNewProduct seharusnya berhasil untuk input valid")
    void addNewProduct_shouldSucceed_forValidInput() {
        boolean result = productController.addNewProduct(
            "Produk Epsilon", "bond", "Sangat Rendah", "Deskripsi Epsilon", new BigDecimal("250.00")
        );
        assertTrue(result, "Penambahan produk seharusnya berhasil.");
        assertEquals(sampleProducts.size() + 1, productController.getAllInvestmentProducts().size(), "Jumlah produk seharusnya bertambah satu.");
    }

    @Test
    @DisplayName("addNewProduct seharusnya gagal untuk tipe produk tidak valid")
    void addNewProduct_shouldFail_forInvalidProductType() {
        boolean result = productController.addNewProduct(
            "Produk Zeta", "crypto", "Sangat Tinggi", "Deskripsi Zeta", new BigDecimal("500.00")
        );
        assertFalse(result, "Penambahan produk seharusnya gagal untuk tipe tidak valid.");
        assertEquals(sampleProducts.size(), productController.getAllInvestmentProducts().size(), "Jumlah produk seharusnya tidak bertambah.");
    }

    @Test
    @DisplayName("addNewProduct seharusnya gagal jika nama produk kosong")
    void addNewProduct_shouldFail_whenProductNameIsEmpty() {
         boolean result = productController.addNewProduct(
            "", "stock", "Sedang", "Deskripsi", new BigDecimal("10.00")
        );
        assertFalse(result, "Penambahan produk seharusnya gagal jika nama kosong.");
    }

    @Test
    @DisplayName("addNewProduct seharusnya gagal jika harga negatif")
    void addNewProduct_shouldFail_whenPriceIsNegative() {
         boolean result = productController.addNewProduct(
            "Produk Eta", "bond", "Rendah", "Deskripsi", new BigDecimal("-100.00")
        );
        assertFalse(result, "Penambahan produk seharusnya gagal jika harga negatif.");
    }
}