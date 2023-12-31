async function signup() {
  let username = document.getElementById("username").value;
  let password = document.getElementById("password").value;

  console.log(username, password);

  const response = await fetch("http://localhost:8080/user", {
    method: "POST",
    headers: new Headers({
      "Content-Type": "application/json; charset=utf8",
      Accept: "application/json",
    }),
    body: JSON.stringify({
      username: username,
      password: password,
    }),
  });

  if (response.ok) {
    console.log(response.status, "resultado response status... <-");
    showToast("#okToast");
    window.setTimeout(function () {
      window.location = "/view/login.html";
    }, 2000);
  } else {
    console.log(response.status, "resultado response status... <-");
    showToast("#errorToast");
    
  }

}
  
function showToast(id) {
  var toastElList = [].slice.call(document.querySelectorAll(id));
  var toastList = toastElList.map(function (toastEl) {
    return new bootstrap.Toast(toastEl);
  });
  toastList.forEach((toast) => toast.show());
}