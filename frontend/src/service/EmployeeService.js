// src/service/EmployeeService.js
import axios from '@/plugins/axios';

export const EmployeeService = {
    async getEmployees({ departmentId, page = 1, size = 10, sort = 'id' } = {}) {
        const params = { departmentId, page, size, sort };
        const response = await axios.get('/api/employees', { params });
        return response.data;
    },

    async getEmployeeById(id) {
        const response = await axios.get(`/api/employees/${id}`);
        return response.data;
    },

    async createEmployee(employee) {
        const response = await axios.post('/api/employees', employee);
        return response.data;
    },

    async updateEmployee(id, employee) {
        const response = await axios.put(`/api/employees/${id}`, employee);
        return response.data;
    },

    async deleteEmployee(id) {
        await axios.delete(`/api/employees/${id}`);
    }
};
