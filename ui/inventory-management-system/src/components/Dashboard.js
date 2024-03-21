import React, { useEffect, useState } from 'react';
import axios from 'axios';
import './Dashboard.css';

class Product {
  constructor(productId, name, description, price, quantity) {
    this.productId = productId;
    this.name = name;
    this.description = description;
    this.price = price;
    this.quantity = quantity;
  }
}

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

  const [showAddProductModal, setShowAddProductModal] = useState(false);
  const [showAddSaleModal, setShowAddSaleModal] = useState(false);

  const [newProductData, setNewProductData] = useState({
    name: '',
    description: '',
    price: '',
    quantity: ''
  });

  const [newSaleData, setNewSaleData] = useState({
    productId: '',
    quantitySold: '',
    saleDate: ''
  });

  const handleAddProduct = async () => {
    try {
      await axios.post('http://localhost:8081/api/products', newProductData, {
        headers: {
          'Authorization': localStorage.getItem('jwtToken'),
          'Content-Type': 'application/json'
        }
      });
      setShowAddProductModal(false);
      fetchData();
    } catch (error) {
      console.error('Error adding product:', error);
    }
  };

  const handleAddSale = async () => {
    try {
      const timestampSaleDate = new Date(newSaleData.saleDate).getTime() + 1;
      const saleData = {
        ...newSaleData,
        saleDate: timestampSaleDate
      };

      await axios.post('http://localhost:8082/api/sales', saleData, {
        headers: {
          'Authorization': localStorage.getItem('jwtToken'),
          'Content-Type': 'application/json'
        }
      });
      setShowAddSaleModal(false);
      fetchData(); 
    } catch (error) {
      console.error('Error adding sale:', error);
    }
  };


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
      const { productId, ...productData } = editProductData;
      await axios.put(`http://localhost:8081/api/products/${editProductData.productId}`, productData, {
        headers: {
          'Authorization': localStorage.getItem('jwtToken'),
          'Content-Type': 'application/json'
        }
      });
      setShowEditProductModal(false);
      fetchData();
    } catch (error) {
      console.error('Error saving product:', error);
    }
  };

  const handleSaveSale = async () => {
    try {
      const timestampSaleDate = new Date(editSaleData.saleDate).getTime() + 1;
      const { saleId, saleDate, ...saleData } = editSaleData;
      const updatedSaleData = {
        ...saleData,
        saleDate: timestampSaleDate
      };

      await axios.put(`http://localhost:8082/api/sales/${editSaleData.saleId}`, updatedSaleData, {
        headers: {
          'Authorization': localStorage.getItem('jwtToken'),
          'Content-Type': 'application/json'
        }
      });
      setShowEditSaleModal(false);
      fetchData();
    } catch (error) {
      console.error('Error saving sale:', error);
    }
  };

  const handleDeleteProduct = async (productId) => {
    try {
      await axios.delete(`http://localhost:8081/api/products/${productId}`, {
        headers: {
          'Authorization': localStorage.getItem('jwtToken'),
          'Content-Type': 'application/json'
        }
      });
      fetchData();
    } catch (error) {
      console.error('Error deleting product:', error);
    }
  };

  const handleDeleteSale = async (saleId) => {
    try {
      await axios.delete(`http://localhost:8082/api/sales/${saleId}`, {
        headers: {
          'Authorization': localStorage.getItem('jwtToken'),
          'Content-Type': 'application/json'
        }
      });
      fetchData();
    } catch (error) {
      console.error('Error deleting sale:', error);
    }
  };

  return (
    <div className="dashboard-container">
      {showAddProductModal && (
        <div className="modal">
          <h3>Add Product</h3>
          <form>
            <label>Product Name:</label>
            <input
              type="text"
              value={newProductData.name}
              onChange={(e) => setNewProductData({ ...newProductData, name: e.target.value })}
            />
            <label>Description:</label>
            <input
              type="text"
              value={newProductData.description}
              onChange={(e) => setNewProductData({ ...newProductData, description: e.target.value })}
            />
            <label>Price:</label>
            <input
              type="number"
              value={newProductData.price}
              onChange={(e) => setNewProductData({ ...newProductData, price: e.target.value })}
            />
            <label>Quantity:</label>
            <input
              type="number"
              value={newProductData.quantity}
              onChange={(e) => setNewProductData({ ...newProductData, quantity: e.target.value })}
            />
          </form>
          <button onClick={() => handleAddProduct()}>Add Product</button>
          <button onClick={() => setShowAddProductModal(false)}>Cancel</button>
        </div>
      )}

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
      <div className="section">
        <h3>Product Inventory</h3>
        {
          showAddProductModal === false && (
            <button onClick={() => setShowAddProductModal(true)}>Add Product</button>
          )
        }
        <table className="table">
          <thead>
            <tr>
              <th>Product ID</th>
              <th>Name</th>
              <th>Description</th>
              <th>Price</th>
              <th>Quantity</th>
              <th>Edit</th>
              <th>Delete</th>
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
                <td>
                  <button onClick={() => handleDeleteProduct(product.productId)}>Delete</button>
                </td>
              </tr>
            ))}
          </tbody>
        </table>
      </div>

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
      {showAddSaleModal && (
        <div className="modal">
          <h3>Add Sale</h3>
          <form>
            <label>Product ID:</label>
            <input
              type="text"
              value={newSaleData.productId}
              onChange={(e) => setNewSaleData({ ...newSaleData, productId: e.target.value })}
            />
            <label>Quantity Sold:</label>
            <input
              type="number"
              value={newSaleData.quantitySold}
              onChange={(e) => setNewSaleData({ ...newSaleData, quantitySold: e.target.value })}
            />
            <label>Sale Date:</label>
            <input
              type="date"
              value={newSaleData.saleDate}
              onChange={(e) => setNewSaleData({ ...newSaleData, saleDate: e.target.value })}
            />
          </form>
          <button onClick={() => handleAddSale()}>Add Sale</button>
          <button onClick={() => setShowAddSaleModal(false)}>Cancel</button>
        </div>
      )}

      <div className="section">
        <h3>Recent Sales</h3>
        {
          showAddProductModal === false && (
            <button onClick={() => setShowAddSaleModal(true)}>Add Product</button>
          )
        }
        <table className="table">
          <thead>
            <tr>
              <th>Sale ID</th>
              <th>Product ID</th>
              <th>Quantity Sold</th>
              <th>Sale Date</th>
              <th>Edit</th>
              <th>Delete</th>
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
                <td>
                  <button onClick={() => handleDeleteSale(sale.saleId)}>Delete</button>
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
