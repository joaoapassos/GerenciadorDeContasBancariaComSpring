async function cadastrarConta(e){
    e.preventDefault();
    const form = e.target;
    
    let conta = {
        numero: 0,
        titular: form.titular.value,
        email: form.email.value,
        senha: form.senha.value,
        saldo: 0.0
    }
    
    const response = await fetch(`${urlServe}/api/contas/corrente/cadastro`, {
        method: "POST",
        body: JSON.stringify(conta),
        headers: {
            "Content-Type": "application/json"
        }
    })
    
    if(response.status !== 200){
        const error = await response.json();
        console.log("Erro ao cadastrar a conta")
        console.error("Erro: " + error);
        alert("Erro ao cadastrar conta\nTente novamente!")
        return;
    }
    
    console.log("Conta Cadastrada!!!")
    window.location.href = "index.html";
}

async function acessarConta(e) {
    e.preventDefault();
    const form = e.target;
    // console.log(form)
    
    let conta = {
        numero: 0,
        titular: "",
        email: form.email.value,
        senha: form.senha.value,
        saldo: 0.0
    }

    // console.log(conta)

    const response = await fetch(`${urlServe}/api/contas/corrente/acessar`, {
        method: "POST",
        body: JSON.stringify(conta),
        headers: {
            "Content-Type": "application/json"
        }
    })

    const data = await response.json()
    
    if(response.status !== 200){
        const error = await response.json();
        console.log("Erro ao acessar a conta")
        console.error("Erro: " + error);
        alert("Erro ao acessar conta: Email ou senha incorretos\nTente novamente!")
        return;
    }
    
    const login = data || {};

    salvarLocalStorageConta(login);

    console.log("Conta Acessada!!!");
    window.location.href = "conta.html";
}

async function carregarConta(){
    let conta = localStorage.getItem("Conta");

    conta = JSON.parse(conta);
    // console.log(conta)

    const response = await fetch(`${urlServe}/api/contas/corrente/${conta.numero}`, {
        method: "GET"
    })
    
    const data = await response.json();

    if(response.status !== 200){
        const error = await response.json();
        console.log("Erro ao acessar a conta")
        console.error("Erro: " + error);
        alert("Erro ao acessar conta")
        return;
    }
    
    salvarLocalStorageConta(data);

    console.log("Conta carregada com sucesso!")

    return data;
}

async function carregarDadosConta(){
    const conta = await carregarConta();

    // console.log(conta)

    const form = document.getElementById("editar-conta");

    document.getElementById("id-conta").innerText = "ID " + conta.numero;
    document.getElementById("saldo-conta").innerText = "Saldo: R$" + conta.saldo;

    form.titular.value = conta.titular;
    form.email.value = conta.email;
    form.senha.value = conta.senha;
}

async function carregarContas() {
    const response = await fetch(`${urlServe}/api/contas/corrente`, {
        method: "GET"
    })
    
    const data = await response.json();

    if(response.status !== 200){
        const error = await response.json();
        console.log("Erro ao acessar a conta")
        console.error("Erro: " + error);
        alert("Erro ao carregar contas")
        return;
    }
    
    console.log("Contas carregadas com sucesso!")

    return data;
}

async function carregarListaMainDeContas(contas) {
    if(!contas){
        contas = await carregarContas();
    }

    let lista = document.getElementById("lista-main");

    lista.innerHTML = "";

    contas.forEach(c => {
      lista.innerHTML += `
        <li>
            <button id="${c.email}" class="conta" onclick="selecionarContas(this)" onblur="contaDeselecionada()">ID ${c.numero} - ${c.titular} - ${c.email} - R$${c.saldo}</button>
        </li>
      `  
    });
    
}

async function carregarListaDeContasInSelect() {
    const contas = await carregarContas();

    let lista = document.getElementById("contas-transacao");

    const conta = JSON.parse(localStorage.getItem("Conta"));

    contas.forEach(c => {
        if(c.numero !== conta.numero){
            lista.innerHTML += `
                <option value="${c.numero}" selected>${c.titular}</option>
            `
        } 
    });

    lista.options[0].selected = true;
}

async function deletarConta() {

    if(!confirm("Deseja deletar essa conta realmente?")) return;

    const conta = JSON.parse(localStorage.getItem("Conta"));

    const response = await fetch(`${urlServe}/api/contas/corrente`, {
        method: "DELETE",
        body: JSON.stringify(conta),
        headers: {
            "Content-Type": "application/json"
        }
    })
    
    if(response.status !== 200){
        const error = await response.json();
        console.log("Erro ao deletar a conta")
        console.error("Erro: " + error);
        alert("Erro ao deletar conta\nTente novamente!")
        return;
    }

    alert("Conta deletada com sucesso!")

    limparLocalStorageConta();
    voltarListaDeContas();
}

async function updateConta(e) {
    let conta = JSON.parse(localStorage.getItem("Conta"));

    const form = e.target;

    conta = {
        numero : conta.numero,
        titular : form.titular.value,
        email : form.email.value,
        senha : form.senha.value,
        saldo : conta.saldo
    }

    const response = await fetch(`${urlServe}/api/contas/corrente`, {
        method: "PUT",
        body: JSON.stringify(conta),
        headers: {
            "Content-Type": "application/json"
        }
    })
    
    if(response.status !== 200){
        const error = await response.json();
        console.log("Erro ao deletar a conta")
        console.error("Erro: " + error);
        alert("Erro ao atualizar contaa\nTente novamente!")
        return;
    }

    console.log("Conta atualizada com sucesso!")

    salvarLocalStorageConta(conta);
}

async function sacar(e) {
    e.preventDefault();
    let conta = JSON.parse(localStorage.getItem("Conta"));
    const form = e.target;
    // console.log(form.valor.value)

    const response = await fetch(`${urlServe}/api/contas/corrente/sacar?valor=${form.valor.value}`, {
        method: "PUT",
        body: JSON.stringify(conta),
        headers: {
            "Content-Type": "application/json"
        }
    })

    if(response.status !== 200){
        const error = await response.json();
        console.log("Erro ao sacar saldo da conta")
        console.error("Erro: " + error.erro);
        alert("Erro ao sacar saldo da conta\nErro: " + error.erro)
        return;
    }
    
    salvarLocalStorageConta(conta);

    window.location.reload();
}

async function depositar(e) {
    e.preventDefault();
    let conta = JSON.parse(localStorage.getItem("Conta"));
    const form = e.target;
    console.log(form.valor.value)

    const response = await fetch(`${urlServe}/api/contas/corrente/depositar?valor=${form.valor.value}`, {
        method: "PUT",
        body: JSON.stringify(conta),
        headers: {
            "Content-Type": "application/json"
        }
    })

    if(response.status !== 200){
        const error = await response.json();
        console.log("Erro ao depositar saldo da conta")
        console.error("Erro: " + error);
        alert("Erro ao depositar saldo da conta\nErro: " + error)
        return;
    }
  
    salvarLocalStorageConta(conta);

    window.location.reload();
}

async function getContaById(id){
    const response = await fetch(`${urlServe}/api/contas/corrente/${id}`, {
        method: "GET"
    })
    
    if(response.status !== 200){
        const error = await response.json();
        console.log("Erro ao buscar conta")
        console.error("Erro: " + error);
        alert("Erro ao buscar conta\nErro: " + error)
        return;
    }

    return await response.json();
}

async function transacao(e) {
    e.preventDefault();
    let conta = JSON.parse(localStorage.getItem("Conta"));
    const form = e.target;

    const contaDestino = form.contas.value;

    const contas = [conta.numero, contaDestino]

    const response = await fetch(`${urlServe}/api/contas/corrente/transacao?valor=${form.valor.value}`, {
        method: "POST",
        body: JSON.stringify(contas),
        headers: {
            "Content-Type": "application/json"
        }
    })
    

    if(response.status !== 200){
        const error = await response.json();
        console.log("Erro realizar transação")
        console.error("Erro: " + error);
        alert("Erro realizar transação\nErro: " + error.erro)
        return;
    }

    window.location.reload();
}

async function carregarTotalDeSaldoDasContas(){
    const response = await fetch(`${urlServe}/api/contas/corrente/saldototal`, {
        method: "GET"
    })
    
    const data = await response.json();

    if(response.status !== 200){
        const error = await response.json();
        console.log("Erro ao acessar a conta")
        console.error("Erro: " + error);
        return;
    }
    
    // console.log(data)

    document.getElementById("total-contas").innerText = "Total: R$" + parseFloat(data);
    document.getElementById("total-contas").title = "Saldo total de todas as contas cadastradas R$" + parseFloat(data);
}