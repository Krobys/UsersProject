# UsersProject

Simple Android app for managing users (Sliide challenge).

## ✅ Implemented

- [x] Display list of users (last page)  
- [x] Show name, email and gender 
- [x] Loading & error states  
- [x] Add new user via dialog (+ button)  
- [x] Remove user via long-press dialog  
- [x] Jetpack Compose UI  
- [x] MVVM with Hilt for DI  
- [x] Kotlin Coroutines  
- [x] Supports device rotation  
- [x] Material Design (Light & Dark theme)  
- [x] Unit tests for mappers, repository, use-cases, ViewModel  

| Users | Add user | Delete user | Dark mode |
|-------|----------|-------------|-----------|
| ![main_list](https://github.com/user-attachments/assets/8f59ac82-1fb6-4e9e-92a7-ebdc79415e9c) | ![add_user](https://github.com/user-attachments/assets/af1075ab-cf1d-4791-8ed0-b150704ec3df) |![remove_user](https://github.com/user-attachments/assets/f5ad1532-13e7-4927-9666-632f15f4469d) |![Screenshot_20250606_173833](https://github.com/user-attachments/assets/8ef97c0e-5996-44ac-9345-a3160d1c45d0) |

## Tests

- `NetworkMapperTest` – GraphQL → DTO mapping  
- `UsersMapperTest` – DTO → Domain mapping  
- `UsersRepositoryImplTest` – repository logic  
- `GetUsersUseCaseTest`, `AddUserUseCaseTest`, `DeleteUserUseCaseTest` – use-case logic  
- `UsersViewModelTest` – ViewModel states & events  

