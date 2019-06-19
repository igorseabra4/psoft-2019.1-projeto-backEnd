class MuralDisciplina extends HTMLElement{
	constructor() {
		super();
		this.$shadow = this.attachShadow({"mode": "open"});
	}

	connectedCallback() {
		this.id = this.getAttribute('id');
		this.name = this.getAttribute('name');
		this.render();
	}

	render() {
		this.$shadow.innerHTML = 
			`<link rel="stylesheet" href="message.css">
			<p>${this.id} - ${this.name}</p>`;
	}
}
window.customElements.define('disciplina-texto', MuralDisciplina);

async function get_messages() {
	console.log(window.location.href);
	let response = await fetch('/api/v1/courses/');
	let data = await response.json();

	let $disciplinas = document.getElementById("disciplinas");
	$disciplinas.innerHTML = '';

	data.forEach(function (message) {
		let novo = document.createElement("disciplina-texto");
		novo.setAttribute('id', message.id);
		novo.setAttribute('name', message.name);
		novo.setAttribute('grade', message.grade);
		$disciplinas.appendChild(novo);
	});
}

async function init() {
	await get_messages();
}

init();
