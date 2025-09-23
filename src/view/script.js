// const url = "127.0.0.1:5500/src/view/";
const urlServe = "http://localhost:8080";
function selecionarContas(e){
    e.focus();
    const btnAcessar = document.getElementById('cadastrar-acessar');
    localStorage.setItem("emailCache", e.id);
    btnAcessar.innerText = "Acessar conta"
}
function contaDeselecionada(){
    setTimeout(()=>{
        const btnAcessar = document.getElementById('cadastrar-acessar');
        btnAcessar.innerText = "Cadastrar nova conta"
    },160)
}

function cadastrarOuAcessarConta(e){
    if(e.innerText === "Cadastrar nova conta"){
        window.location.href = "cadastro.html";
    }
    else if(e.innerText === "Acessar conta"){
        window.location.href = "acessar.html";    
    }
}

function voltarListaDeContas(){
    window.location.href = "index.html";
    localStorage.removeItem("emailCache");
}

function abrirMenuServices(e){
    const aside = document.getElementById("menu-services");
    let service;
    if(e.innerText == "Filtrar") service = "filtro";
    else if(e.innerText == "Ordenar") service = "ordenacao";
    else if(e.innerText == "Agrupar") service = "agrupar";
    else if(e.innerText == "Sacar") service = "sacar";
    else if(e.innerText == "Depositar") service = "depositar";
    else if(e.innerText == "Transferir") service = "transacao";
    
    if(aside.style.display == "block") fecharMenuServices(service);
    
    e.classList.add("btn-services-active");
    
    const asideService = document.getElementById("aside-" + service);


    aside.style.display = "block";
    asideService.style.display = "block"

    const sair = document.getElementById("sair-menu-services");

    sair.addEventListener('click', ()=>{
        aside.style.display = "none";
        asideService.style.display = "none"
        e.classList.remove("btn-services-active")
    })
}

function fecharMenuServices(service){

    if(service == "filtro" || service == "ordenacao" || service == "agrupar"){
        document.getElementById("aside-filtro").style.display = "none";
        document.getElementById("aside-ordenacao").style.display = "none";
        document.getElementById("aside-agrupar").style.display = "none";
        document.getElementById("btn-filtrar").classList.remove("btn-services-active");
        document.getElementById("btn-ordenar").classList.remove("btn-services-active");
        document.getElementById("btn-agrupar").classList.remove("btn-services-active");
    }
    else{
        document.getElementById("aside-sacar").style.display = "none";
        document.getElementById("aside-depositar").style.display = "none";
        document.getElementById("aside-transacao").style.display = "none";
        document.getElementById("btn-sacar").classList.remove("btn-services-active");
        document.getElementById("btn-depositar").classList.remove("btn-services-active");
        document.getElementById("btn-transacao").classList.remove("btn-services-active");
    }

}

function salvarLocalStorageConta(conta){
    localStorage.setItem("Conta", JSON.stringify(conta));
}

function limparLocalStorageConta(){
    localStorage.removeItem("Conta");
}


{
const themeToggleBtn = document.getElementById('alterar-tema');
const htmlElement = document.documentElement; // O elemento <html>

// --- LÓGICA AO CARREGAR A PÁGINA ---

// 1. Verifica se o usuário JÁ escolheu um tema manualmente
const savedTheme = localStorage.getItem('theme');

// 2. Verifica qual é a preferência do sistema operacional/navegador
const systemPrefersDark = window.matchMedia('(prefers-color-scheme: dark)').matches;

// A decisão de qual tema usar segue esta prioridade:
// a) A escolha salva pelo usuário (se existir)
// b) A preferência do sistema (se não houver escolha salva)
// c) Padrão para o tema claro
if (savedTheme === 'dark') {
    htmlElement.classList.add('dark-mode');
} else if (savedTheme === 'light') {
    htmlElement.classList.remove('dark-mode');
} else if (systemPrefersDark) {
    // Se não há nada salvo, mas o sistema prefere escuro, ativamos o dark mode
    htmlElement.classList.add('dark-mode');
}


// --- LÓGICA DO CLIQUE NO BOTÃO ---

themeToggleBtn.addEventListener('click', () => {
    // Verifica se o dark mode está ativo no momento do clique
    const isDarkMode = htmlElement.classList.contains('dark-mode');

    console.log("Btn clicado")

    if (isDarkMode) {
        // Se estiver ativo, remove a classe e salva a preferência 'light'
        htmlElement.classList.remove('dark-mode');
        localStorage.setItem('theme', 'light');
        console.log("Light mode")
    } else {
        // Se não estiver ativo, adiciona a classe e salva a preferência 'dark'
        htmlElement.classList.add('dark-mode');
        localStorage.setItem('theme', 'dark');
        console.log("Dark mode")
    }
});
}