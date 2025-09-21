async function aplicarFiltro(e) {
    e.preventDefault();
    const contas = await carregarContas();
    const form = e.target;

    const checkboxs = form.querySelectorAll("input[type=checkbox]");
    let type;
    // console.log(checkboxs)
    let checkboxsCount = 0;
    checkboxs.forEach(c => {
        if(c.checked == true){
            checkboxsCount++;
            type = document.querySelector(`label[for=${c.id}]`).innerText;
        }
    })

    if(checkboxsCount > 1){
        alert("Para aplicar um filtro selecione apenas um de cada vez");
        return;
    }

    else if(checkboxsCount < 1){
        alert("Para aplicar um filtro selecione um primeiro");
        return;
    }
    
    else if(form.filtro2.checked == true && form.valorFiltro2.value == ""){
        alert('Para aplicar o filtro: "Filtrar contas com saldo maior que:" preencha o campo de valor do saldo');
        return;
    }

    let response;

    if(type == "Filtrar contas com numero par"){
        response = await fetch(`${urlServe}/api/services/corrente/filter/FiltroByNumberPar`, {
            method: "POST",
            body: JSON.stringify(contas),
            headers: {
                "Content-Type": "application/json"
            }
        })
    }
    else if(type == "Filtrar contas com saldo maior que:"){
        response = await fetch(`${urlServe}/api/services/corrente/filter/FiltroBySaldoMaiorQ?saldoMin=${form.valorFiltro2.value}`, {
            method: "POST",
            body: JSON.stringify(contas),
            headers: {
                "Content-Type": "application/json"
            }
        })
    }
    
    if(response.status !== 200){
        const error = await response.json();
        console.log("Erro ao filtrar as contas")
        console.error("Erro: " + error);
        return;
    }

    const data = await response.json();

    carregarListaMainDeContas(data);

    document.getElementById("p-descricao-servico-aplicado").innerText = `Filtro aplicado: ${type}`;
}

async function aplicarOrdenacao(e) {
    e.preventDefault();
    const contas = await carregarContas();
    const form = e.target;

    const checkboxs = form.querySelectorAll("input[type=checkbox]");
    let type;
    // console.log(checkboxs)
    let checkboxsCount = 0;
    checkboxs.forEach(c => {
        if(c.checked == true){
            checkboxsCount++;
            type = document.querySelector(`label[for=${c.id}]`).innerText;
        }
    })

    if(checkboxsCount > 1){
        alert("Para aplicar uma ordenação selecione apenas uma de cada vez");
        return;
    }

    else if(checkboxsCount < 1){
        alert("Para aplicar uma ordenação selecione uma primeiro");
        return;
    }

    let response;

    if(type == "Ordenar contas por nome (A-Z)"){
        response = await fetch(`${urlServe}/api/services/corrente/order/OrdenacaoByName`, {
            method: "POST",
            body: JSON.stringify(contas),
            headers: {
                "Content-Type": "application/json"
            }
        })
    }
    else if(type == "Ordenar contas por saldo (decrecente)"){
        response = await fetch(`${urlServe}/api/services/corrente/order/OrdenacaoDecrecente`, {
            method: "POST",
            body: JSON.stringify(contas),
            headers: {
                "Content-Type": "application/json"
            }
        })
    }
    
    if(response.status !== 200){
        const error = await response.json();
        console.log("Erro ao ordenar as contas")
        console.error("Erro: " + error);
        return;
    }

    const data = await response.json();

    carregarListaMainDeContas(data);

    document.getElementById("p-descricao-servico-aplicado").innerText = `Ordenação aplicada: ${type}`;
}

async function aplicarAgrupamento(e) {
    e.preventDefault();
    const contas = await carregarContas();
    const form = e.target;

    const checkboxs = form.querySelectorAll("input[type=checkbox]");
    let type;
    // console.log(checkboxs)
    let checkboxsCount = 0;
    checkboxs.forEach(c => {
        if(c.checked == true){
            checkboxsCount++;
            type = document.querySelector(`label[for=${c.id}]`).innerText;
        }
    })

    if(checkboxsCount > 1){
        alert("Para aplicar um agrupamento selecione apenas um de cada vez");
        return;
    }

    else if(checkboxsCount < 1){
        alert("Para aplicar um agrupamento selecione um primeiro");
        return;
    }
    
    else if(form.agrupamentos2.checked == true && (form.agrupamentos2value1.value == "" || form.agrupamentos2value2.value == "")){
        alert('Para aplicar o Agrupamento: "Agrupar contas com faixa de saldo personalizada:" preencha os campos de valor minimo e maximo do saldo');
        return;
    }

    let response;
    let data;
    let arrayContas;

    if(type == "Agrupar contas por faixas de saldo"){
        response = await fetch(`${urlServe}/api/services/corrente/groupby/AgruparContasPorSaldo`, {
            method: "POST",
            body: JSON.stringify(contas),
            headers: {
                "Content-Type": "application/json"
            }
        })

        data = await response.json();

        arrayContas = [
            {
                legenda : "Saldo até R$ 5.000",
                contas : data[0]
            },
            {
                legenda : "Saldo de R$ 5.001 a R$ 10.000",
                contas : data[1]
            },
            {
                legenda : "Saldo acima de R$ 10.000",
                contas : data[2]
            }
        ]

        console.log(arrayContas)
    }
    else if(type == "Agrupar contas com faixa de saldo personalizada:"){
        response = await fetch(`${urlServe}/api/services/corrente/groupby/AgruparContasPorFaixaDeSaldo?min=${form.agrupamentos2value1.value}&max=${form.agrupamentos2value2.value}`, {
            method: "POST",
            body: JSON.stringify(contas),
            headers: {
                "Content-Type": "application/json"
            }
        })

        data = await response.json();

        arrayContas = [
            {
                legenda : `Saldo dentre R$${form.agrupamentos2value1.value} e R$${form.agrupamentos2value2.value}`,
                contas : data[0]
            }
        ]

        console.log(arrayContas)
    }
    
    if(response.status !== 200){
        const error = await response.json();
        console.log("Erro ao agrupar as contas")
        console.error("Erro: " + error);
        return;
    } 



    // console.log(data)

    carregarListaMainDeContasAgrupada(arrayContas);

    document.getElementById("p-descricao-servico-aplicado").innerText = `Agrupamento aplicado: ${type}`;
}

function carregarListaMainDeContasAgrupada(arrayContas){
    
    let lista = document.getElementById("lista-main");

    lista.innerHTML = "";

    console.log(arrayContas)

    arrayContas.forEach(contas =>{
        console.log(contas)
        if(contas.contas == undefined){
            lista.innerHTML += `
                <div>
                    <legend id="legenda-agrupamento">${contas.legenda}</legend>
                    <li>
                        Sem resultados
                    </li>
                </div>
            `  
        }
        else{
            let div = document.createElement("div");

            div.innerHTML += `<legend id="legenda-agrupamento">${contas.legenda}</legend>`;

            contas.contas.forEach(c => {
            
            div.innerHTML += ` 
                <li>
                    <button id="${c.email}" class="conta" onclick="selecionarContas(this)" onblur="contaDeselecionada()">ID ${c.numero} - ${c.titular} - ${c.email} - R$${c.saldo}</button>
                </li>
            `  
            })

            lista.appendChild(div);
        }
    })
}