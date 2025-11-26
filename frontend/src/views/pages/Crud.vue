<script setup>
import { ref, onMounted } from 'vue';
import { useToast } from 'primevue/usetoast';
import { EmployeeService } from '@/service/EmployeeService';
import { FilterMatchMode } from '@primevue/core/api';
import DataTable from 'primevue/datatable';
import Column from 'primevue/column';
import Toolbar from 'primevue/toolbar';
import Button from 'primevue/button';
import InputText from 'primevue/inputtext';
import Dialog from 'primevue/dialog';
import Checkbox from 'primevue/checkbox';
import Password from 'primevue/password';

const toast = useToast();
const dt = ref(null);
const employees = ref([]);
const employeeDialog = ref(false);
const deleteEmployeeDialog = ref(false);
const deleteEmployeesDialog = ref(false);
const employee = ref({});
const selectedEmployees = ref([]);
const submitted = ref(false);
const filters = ref({
    global: { value: null, matchMode: FilterMatchMode.CONTAINS }
});
const totalRecords = ref(0);
const rows = ref(10);
const first = ref(0);
const sortField = ref('id');
const sortOrder = ref(1); // 1 = asc, -1 = desc
// Load employees on mount
onMounted(() => loadEmployeesLazy());

async function loadEmployeesLazy(event = {}) {
    try {
        const page = event.first ? event.first / event.rows : 0;
        const size = event.rows || rows.value;
        const sort = event.sortField ? `${event.sortField},${event.sortOrder === 1 ? 'asc' : 'desc'}` : 'id,asc';

        const data = await EmployeeService.getEmployees({ page, size, sort });
        employees.value = data.content || data;
        totalRecords.value = data.totalElements || employees.value.length;
    } catch (err) {
        toast.add({ severity: 'error', summary: 'Error', detail: 'Failed to load employees', life: 3000 });
        console.error(err);
    }
}

function onPage(event) {
    loadEmployeesLazy(event);
}

function onSort(event) {
    loadEmployeesLazy(event);
}

function openNew() {
    employee.value = {};
    submitted.value = false;
    employeeDialog.value = true;
}

function hideDialog() {
    employeeDialog.value = false;
    submitted.value = false;
}

async function saveEmployee() {
    submitted.value = true;
    if (!employee.value.firstName?.trim() || !employee.value.lastName?.trim()) return;

    try {
        if (employee.value.id) {
            const updated = await EmployeeService.updateEmployee(employee.value.id, employee.value);
            const index = employees.value.findIndex(e => e.id === updated.id);
            if (index !== -1) employees.value[index] = updated;
            toast.add({ severity: 'success', summary: 'Updated', detail: 'Employee updated', life: 3000 });
        } else {
            const created = await EmployeeService.createEmployee(employee.value);
            employees.value.push(created);
            toast.add({ severity: 'success', summary: 'Created', detail: 'Employee created', life: 3000 });
        }
        hideDialog();
        employee.value = {};
    } catch (err) {
        toast.add({ severity: 'error', summary: 'Error', detail: 'Failed to save employee', life: 3000 });
        console.error(err);
    }
}

function editEmployee(emp) {
    employee.value = { ...emp };
    employeeDialog.value = true;
}

function confirmDeleteEmployee(emp) {
    employee.value = emp;
    deleteEmployeeDialog.value = true;
}

async function deleteEmployee() {
    try {
        await EmployeeService.deleteEmployee(employee.value.id);
        employees.value = employees.value.filter(e => e.id !== employee.value.id);
        deleteEmployeeDialog.value = false;
        selectedEmployees.value = selectedEmployees.value.filter(e => e.id !== employee.value.id);
        toast.add({ severity: 'success', summary: 'Deleted', detail: 'Employee deleted', life: 3000 });
    } catch (err) {
        toast.add({ severity: 'error', summary: 'Error', detail: 'Failed to delete employee', life: 3000 });
        console.error(err);
    }
}

function confirmDeleteSelected() {
    deleteEmployeesDialog.value = true;
}

async function deleteSelectedEmployees() {
    try {
        const ids = selectedEmployees.value.map(e => e.id);
        await Promise.all(ids.map(id => EmployeeService.deleteEmployee(id)));
        employees.value = employees.value.filter(e => !ids.includes(e.id));
        selectedEmployees.value = [];
        deleteEmployeesDialog.value = false;
        toast.add({ severity: 'success', summary: 'Deleted', detail: 'Selected employees deleted', life: 3000 });
    } catch (err) {
        toast.add({ severity: 'error', summary: 'Error', detail: 'Failed to delete selected employees', life: 3000 });
        console.error(err);
    }
}
</script>

<template>
    <div>
        <div class="card">
            <Toolbar class="mb-6">
                <template #start>
                    <Button label="New" icon="pi pi-plus" class="mr-2" @click="openNew" />
                    <Button label="Delete" icon="pi pi-trash" @click="confirmDeleteSelected" :disabled="!selectedEmployees.length" />
                </template>
                <template #end>
                    <Button label="Export" icon="pi pi-upload" @click="dt.value?.exportCSV()" />
                </template>
            </Toolbar>

            <DataTable
                ref="dt"
                v-model:selection="selectedEmployees"
                :value="employees"
                dataKey="id"
                selectionMode="multiple"
                :paginator="true"
                :rows="rows"
                :totalRecords="totalRecords"
                :lazy="true"
                @page="onPage"
                @sort="onSort"
                :sortField="sortField"
                :sortOrder="sortOrder"
                paginatorTemplate="FirstPageLink PrevPageLink PageLinks NextPageLink LastPageLink CurrentPageReport RowsPerPageDropdown"
                :rowsPerPageOptions="[5,10,25]"
                currentPageReportTemplate="Showing {first} to {last} of {totalRecords} employees"
            >

                <template #header>
                    <div class="flex justify-between items-center">
                        <h4 class="m-0">Manage Employees</h4>
                        <InputText v-model="filters.global.value" placeholder="Search..." class="w-1/3" />
                    </div>
                </template>

                <Column selectionMode="multiple" style="width: 3rem"></Column>
                <Column field="firstName" header="First Name" sortable></Column>
                <Column field="lastName" header="Last Name" sortable></Column>
                <Column field="middleName" header="Middle Name" sortable></Column>
                <Column field="roleInCompany" header="Role" sortable></Column>
                <Column field="hiredAt" header="Hired At" sortable></Column>
                <Column field="isOnHoliday" header="Holiday" sortable>
                    <template #body="slotProps">
                        <span>{{ slotProps.data.isOnHoliday ? 'Yes' : 'No' }}</span>
                    </template>
                </Column>
                <Column :exportable="false">
                    <template #body="slotProps">
                        <Button icon="pi pi-pencil" outlined rounded class="mr-2" @click="editEmployee(slotProps.data)" />
                        <Button icon="pi pi-trash" outlined rounded severity="danger" @click="confirmDeleteEmployee(slotProps.data)" />
                    </template>
                </Column>
            </DataTable>
        </div>

        <!-- Employee Dialog -->
        <Dialog v-model:visible="employeeDialog" header="Employee Details" :modal="true" :style="{ width: '450px' }">
            <div class="flex flex-col gap-4">
                <InputText v-model.trim="employee.firstName" placeholder="First Name" />
                <InputText v-model.trim="employee.lastName" placeholder="Last Name" />
                <InputText v-model.trim="employee.middleName" placeholder="Middle Name" />
                <InputText v-model.trim="employee.roleInCompany" placeholder="Role" />
                <InputText type="date" v-model="employee.hiredAt" />
                <Checkbox v-model="employee.isOnHoliday" /> On Holiday
            </div>
            <template #footer>
                <Button label="Cancel" text icon="pi pi-times" @click="hideDialog" />
                <Button label="Save" icon="pi pi-check" @click="saveEmployee" />
            </template>
        </Dialog>

        <!-- Delete Dialogs -->
        <Dialog v-model:visible="deleteEmployeeDialog" header="Confirm" :modal="true" :style="{ width: '400px' }">
            <p>Are you sure you want to delete <b>{{ employee.firstName }} {{ employee.lastName }}</b>?</p>
            <template #footer>
                <Button label="No" text icon="pi pi-times" @click="deleteEmployeeDialog = false" />
                <Button label="Yes" icon="pi pi-check" @click="deleteEmployee" />
            </template>
        </Dialog>

        <Dialog v-model:visible="deleteEmployeesDialog" header="Confirm" :modal="true" :style="{ width: '400px' }">
            <p>Are you sure you want to delete the selected employees?</p>
            <template #footer>
                <Button label="No" text icon="pi pi-times" @click="deleteEmployeesDialog = false" />
                <Button label="Yes" icon="pi pi-check" @click="deleteSelectedEmployees" />
            </template>
        </Dialog>
    </div>
</template>
