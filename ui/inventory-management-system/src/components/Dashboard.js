import React, { useEffect, useState } from 'react';
import axios from 'axios';
import './Dashboard.css';

// Define Product class
class Product {
  constructor(productId, name, description, price, quantity) {
    this.productId = productId;
    this.name = name;
    this.description = description;
    this.price = price;
    this.quantity = quantity;
  }
}

// Define Sale class
class Sale {
  constructor(saleId, productId, quantitySold, saleDate) {
    this.saleId = saleId;
    this.productId = productId;
    this.quantitySold = quantitySold;
    this.saleDate = saleDate;
  }
}

const Dashboard = () => {
  const [products, setProducts] = useState([]);
  const [sales, setSales] = useState([]);
  const [showEditProductModal, setShowEditProductModal] = useState(false);
  const [showEditSaleModal, setShowEditSaleModal] = useState(false);
  const [editProductData, setEditProductData] = useState(null);
  const [editSaleData, setEditSaleData] = useState(null);

  const fetchData = async () => {
    try {
      const productsResponse = await axios.get('http://localhost:8081/api/products', {
        headers: {
          'Authorization': localStorage.getItem('jwtToken'),
          'Content-Type': 'application/json'
        }
      });
      const productsData = productsResponse.data.map(product => new Product(
        product.productId,
        product.name,
        product.description,
        product.price,
        product.quantity
      ));
      setProducts(productsData);

      const salesResponse = await axios.get('http://localhost:8082/api/sales', {
        headers: {
          'Authorization': localStorage.getItem('jwtToken'),
          'Content-Type': 'application/json'
        }
      });
      const salesData = salesResponse.data.map(sale => new Sale(
        sale.saleId,
        sale.productId,
        sale.quantitySold,
        sale.saleDate
      ));
      setSales(salesData);
    } catch (error) {
      console.error('Error fetching data:', error);
    }
  };

  useEffect(() => {
    fetchData();
  }, []);

  const handleEditProduct = (productId) => {
    const productToEdit = products.find(product => product.productId === productId);
    setEditProductData(productToEdit);
    setShowEditProductModal(true);
    setShowEditSaleModal(false);
  };

  const handleEditSale = (saleId) => {
    const saleToEdit = sales.find(sale => sale.saleId === saleId);
    setEditSaleData(saleToEdit);
    setShowEditSaleModal(true);
    setShowEditProductModal(false);
  };

  const handleSaveProduct = async () => {
    try {
      const { productId, ...productData } = editProductData; // Exclude productId
      await axios.put(`http://localhost:8081/api/products/${editProductData.productId}`, productData, {
        headers: {
          'Authorization': localStorage.getItem('jwtToken'),
          'Content-Type': 'application/json'
        }
      });
      setShowEditProductModal(false);
      fetchData(); // Reload products data after saving
    } catch (error) {
      console.error('Error saving product:', error);
    }
  };
  
  const handleSaveSale = async () => {
    try {
      const { saleId, ...saleData } = editSaleData; // Exclude saleId
      await axios.put(`http://localhost:8082/api/sales/${editSaleData.saleId}`, saleData, {
        headers: {
          'Authorization': localStorage.getItem('jwtToken'),
          'Content-Type': 'application/json'
        }
      });
      setShowEditSaleModal(false);
      fetchData(); // Reload sales data after saving
    } catch (error) {
      console.error('Error saving sale:', error);
    }
  };
  

  return (
    <div className="dashboard-container">
{/* Edit Product Modal */}
{showEditProductModal && (
  <div className="modal">
    <h3>Edit Product</h3>
    <form>
      <label>Product Name:</label>
      <input 
        type="text" 
        defaultValue={editProductData.name} 
        onChange={(e) => setEditProductData({ ...editProductData, name: e.target.value })} 
      />
      <label>Description:</label>
      <input 
        type="text" 
        defaultValue={editProductData.description} 
        onChange={(e) => setEditProductData({ ...editProductData, description: e.target.value })} 
      />
      <label>Price:</label>
      <input 
        type="number" 
        defaultValue={editProductData.price} 
        onChange={(e) => setEditProductData({ ...editProductData, price: e.target.value })} 
      />
      <label>Quantity:</label>
      <input 
        type="number" 
        defaultValue={editProductData.quantity} 
        onChange={(e) => setEditProductData({ ...editProductData, quantity: e.target.value })} 
      />
    </form>
    <button onClick={() => handleSaveProduct()}>Save Changes</button>
    <button onClick={() => setShowEditProductModal(false)}>Cancel</button>
  </div>
)}

{/* Edit Sale Modal */}
{showEditSaleModal && (
  <div className="modal">
    <h3>Edit Sale</h3>
    <form>
      <label>Product ID:</label>
      <input 
        type="text" 
        defaultValue={editSaleData.productId} 
        onChange={(e) => setEditSaleData({ ...editSaleData, productId: e.target.value })} 
      />
      <label>Quantity Sold:</label>
      <input 
        type="number" 
        defaultValue={editSaleData.quantitySold} 
        onChange={(e) => setEditSaleData({ ...editSaleData, quantitySold: e.target.value })} 
      />
      <label>Sale Date:</label>
      <input 
        type="date" 
        defaultValue={editSaleData.saleDate} 
        onChange={(e) => setEditSaleData({ ...editSaleData, saleDate: e.target.value })} 
      />
    </form>
    <button onClick={() => handleSaveSale()}>Save Changes</button>
    <button onClick={() => setShowEditSaleModal(false)}>Cancel</button>
  </div>
)}

      {/* Product Inventory */}
      <div className="section">
        <h3>Product Inventory</h3>
        <table className="table">
          <thead>
            <tr>
              <th>Product ID</th>
              <th>Name</th>
              <th>Description</th>
              <th>Price</th>
              <th>Quantity</th>
              <th>Edit</th>
            </tr>
          </thead>
          <tbody>
            {products.map(product => (
              <tr key={product.productId}>
                <td>{product.productId}</td>
                <td>{product.name}</td>
                <td>{product.description}</td>
                <td>{product.price}</td>
                <td>{product.quantity}</td>
                <td>
                  <button onClick={() => handleEditProduct(product.productId)}>Edit</button>
                </td>
              </tr>
            ))}
          </tbody>
        </table>
      </div>

      {/* Recent Sales */}
      <div className="section">
        <h3>Recent Sales</h3>
        <table className="table">
          <thead>
            <tr>
              <th>Sale ID</th>
              <th>Product ID</th>
              <th>Quantity Sold</th>
              <th>Sale Date</th>
              <th>Edit</th>
            </tr>
          </thead>
          <tbody>
            {sales.map(sale => (
              <tr key={sale.saleId}>
                <td>{sale.saleId}</td>
                <td>{sale.productId}</td>
                <td>{sale.quantitySold}</td>
                <td>{new Date(sale.saleDate).toLocaleDateString()}</td>
                <td>
                  <button onClick={() => handleEditSale(sale.saleId)}>Edit</button>
                </td>
              </tr>
            ))}
          </tbody>
        </table>
      </div>
    </div>
  );
};

export default Dashboard;
