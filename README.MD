<div>Разработан rest service RegistryApp. Приложение содержит 2 таблицы: User и Department.</div>
<div>User относится к сотрудникам. Department относится к отделам.</div>
<div>User и Department связаны отношением многие к одному.</div>
<p>
<div>Просмотр,добавление,поиск,редактирование и удаление данных в User отвечает контроллер UserController</div>
<div>api/user/all просмотр всех сотрудников</div>
<div>api/user/create создание сотрудника</div>
<div>api/user/createall создание списка сотрудников</div>
<div>api/user/search поиск сотрудников</div>
<div>api/user/update редактирование сотрудника</div>
<div>api/user/delete удаление сотрудника</div>
<p>
<div>Просмотр,добавление,поиск,редактирование и удаление данных в Department отвечает контроллер DepartmentControlle</div>
<div>api/department/all просмотр всех департаментов</div>
<div>api/department/search поиск департамента</div>
<div>api/department/create создание департамента</div>
<div>api/department/createall создание списка департаментов</div>
<div>api/department/delete удаление департамента</div>
<p>
<div>Образ: docker pull dmitry735/registryapp-app:latest</div>