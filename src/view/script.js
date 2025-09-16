// Aguarda o carregamento completo do HTML antes de executar o script
function selecionarContas(e){
    e.focus();
    const btnAcessar = document.getElementById('cadastrar-acessar');
    btnAcessar.innerText = "Acessar conta"
}
function contaDeselecionada(){
    setTimeout(()=>{
        const btnAcessar = document.getElementById('cadastrar-acessar');
        btnAcessar.innerText = "Cadastrar nova conta"
    },500)
}

function cadastrarOuAcessarConta(e){
    const painel = document.getElementById("painel-principal");
    painel.style.display = "none";
    const form = document.getElementById("form-cadastro-acessar");
    form.style.display = "flex";
    if(e.innerText === "Cadastrar nova conta"){
        document.getElementById("cadastrar-conta").style.display = "flex";
    }
    else if(e.innerText === "Acessar conta"){
        document.getElementById("acessar-conta").style.display = "flex";
    }
}

function voltarListaDeContas(){
    const painel = document.getElementById("painel-principal");
    painel.style.display = "flex";
    const form = document.getElementById("form-cadastro-acessar");
    form.style.display = "none";
    
    document.getElementById("cadastrar-conta").style.display = "none";
    document.getElementById("acessar-conta").style.display = "none";
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