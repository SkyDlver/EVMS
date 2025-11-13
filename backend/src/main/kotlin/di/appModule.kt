package team.mediagroup.di

import org.koin.dsl.module
import team.mediagroup.repositories.EmployeeRepository
import team.mediagroup.services.EmployeeService
import team.mediagroup.services.AuthService
import team.mediagroup.services.AdminService

val appModule = module {
    single { EmployeeRepository() }
    single { EmployeeService(get()) }   // get() injects EmployeeRepository
    single { AuthService("super-secret-key") }
    single { AdminService(get()) }      // get() injects AuthService
}
