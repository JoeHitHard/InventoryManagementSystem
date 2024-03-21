import React, { useEffect, useState } from 'react';
import axios from 'axios';
import './Dashboard.css';

const Dashboard = () => {
  const [products, setProducts] = useState([]);
  const [sales, setSales] = useState([]);

  useEffect(() => {
    const fetchData = async () => {
      try {
        const productsResponse = await axios.get('http://localhost:8081/api/products', {
        headers: {
          'Authorization': localStorage.getItem('jwtToken'),
          'Content-Type': 'application/json'
        }
      });
      const productsData = productsResponse.data;
      setProducts(productsData);
  
      const salesResponse = await axios.get('http://localhost:8082/api/sales', {
        headers: {
          'Authorization': localStorage.getItem('jwtToken'),
          'Content-Type': 'application/json'
        }
      });
      const salesData = salesResponse.data;
      setSales(salesData);
      } catch (error) {
        console.error('Error fetching data:', error);
      }
    };

    fetchData();
  }, []);

  return (
    <div className="dashboard-container">
      <h2>Dashboard</h2>
      
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
            </tr>
          </thead>
          <tbody>
            {sales.map(sale => (
              <tr key={sale.saleId}>
                <td>{sale.saleId}</td>
                <td>{sale.productId}</td>
                <td>{sale.quantitySold}</td>
                <td>{new Date(sale.saleDate).toLocaleDateString()}</td>
              </tr>
            ))}
          </tbody>
        </table>
      </div>
    </div>
  );
};

export default Dashboard;
