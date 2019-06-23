async function login() {
	let user = {
		email :document.getElementById("email").value,
		password :document.getElementById("password").value
	}
	
	let url = '/api/v1/auth/login';
	
	fetch(url, {
		method: 'POST',
		body: JSON.stringify(user),
		headers: new Headers
		({
			'Accept': 'application/json',
			'Content-Type': 'application/json'
		}),
	})
	.then(result => {
		let localtoken = result.json().token;
		alert(localtoken);
		localStorage.setItem('token', localtoken);
	})
	.catch(response => alert(response));
}

document.getElementById("commit").addEventListener("click", login, false);
