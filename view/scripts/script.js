const tasksEndpoint = "http://localhost:8080/task/user";

function hideLoader() {
  document.getElementById("loading").style.display = "none";
}

function show(tasks) {
  let tab = 
    ` 
    `;
  for (let task of tasks) {
    tab += 
      ` 
      <div class="col" >
        <div class="card" >    
          <div class="card-body">
            <h5 class="card-title">${task.id} - Tarefa </h5>
            <p class="card-text">${task.description}</p>
            <button 
              class="btn btn-sm btn-outline-danger" 
              type="button" 
              id="${task.id}" 
              onclick="deleteTask(${task.id})" >
                Excluir
            </button>
          </div>
        </div>
      </div>
      `;
  }
  document.getElementById("tasks").innerHTML = tab;
}

async function getTasks() {
  let key = "Authorization";
  const response = await fetch(tasksEndpoint, {
    method: "GET",
    headers: new Headers({
      Authorization: localStorage.getItem(key),
    }),
  });

  var data = await response.json();
  console.log(data);
  if (response) hideLoader();
  show(data);
}

async function addTask() {
  let key = "Authorization";
  let description = document.getElementById("description").value;

  console.log(description);

  const response = await fetch("http://localhost:8080/task", {
    method: "POST",
    headers: new Headers({
      Authorization: localStorage.getItem(key),
      "Content-Type": "application/json; charset=utf8",
      Accept: "application/json",
    }),
    body: JSON.stringify({
      description: description,
    }),
  });

  if (response.ok) {
    showToast("#okToast");
    window.setTimeout(function () {
      window.location = "/view/index.html";
    }, 1000);
  } else {
    showToast("#errorToast");
    window.setTimeout(function () {
      window.location = "/view/newTask.html";
    }, 1000);
  }

}

async function deleteTask(id) {

  let key = "Authorization";
  var pathnew = "http://localhost:8080/task/"
  var DelId = id;
  console.log(id); 

  const response = await fetch(pathnew.concat(DelId), {
    method: "DELETE",
    headers: new Headers({
      Authorization: localStorage.getItem(key),
      "Content-Type": "application/json; charset=utf8",
      Accept: "application/json",
    }),
  });

  if (response.ok) {
    showToast("#okToast");
  } else {
    showToast("#errorToast");
  }

  window.setTimeout(function () {
    window.location = "/view/index.html";
  }, 500);

}

function showToast(id) {
  var toastElList = [].slice.call(document.querySelectorAll(id));
  var toastList = toastElList.map(function (toastEl) {
    return new bootstrap.Toast(toastEl);
  });
  toastList.forEach((toast) => toast.show());
}

document.addEventListener("DOMContentLoaded", function (event) {
  if (!localStorage.getItem("Authorization"))
    window.location = "/view/login.html";
});

getTasks();