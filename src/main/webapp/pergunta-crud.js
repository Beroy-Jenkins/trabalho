var $s;
var vm;

const url_pesquisar = 'rest/pergunta/pesquisa';
const url_getById = 'rest/pergunta/getbyid';
const url_remover = 'rest/pergunta/remove';
const url_salvar = 'rest/pergunta/cadastro';


var app = angular.module("app",[]);
	



//diretiva para resolver o post dos botoes
app.directive('eatClick', function() {
    return function(scope, element, attrs) {
        $(element).click(function(event) {
            event.preventDefault();
        });
    }
});



app.controller("ctrl", function ($scope, $http, $timeout) {
		
	$s = $scope;
	
	vm = this;
	$s.http = $http;
	
	//$s.http = httpMock;
	
	//inicia o form de pesquisa
	$s.pes = {text:""};
	//contera a lista de perguntas consultadas
	$s.pesquisa = {items:[]};
	$s.formEdicao = {};
	
	$s.aba = 'pesquisa';


	$('body').show();


});


//https://hello-angularjs.appspot.com/removetablerow