window.addEventListener('load', function () {

    //Al cargar la pagina buscamos y obtenemos el formulario donde estarán
    //los datos que el usuario cargará del nuevo turno
    const formulario = document.querySelector('#add_new_turno');

    //Ante un submit del formulario se ejecutará la siguiente funcion
    formulario.addEventListener('submit', function (event) {
        event.preventDefault();
       //creamos un JSON que tendrá los datos del nuevo turno
        const formData = {
            fecha: document.querySelector('#fecha').value,
            paciente:{
                    id:document.querySelector('#paciente').value,
            },
            odontologo:{
                id:document.querySelector('#odontologo').value,
            },
        };
        //invocamos utilizando la función fetch la API turno con el método POST que guardará
        //el turno que enviaremos en formato JSON
        const url = '/turnos';
        const settings = {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json',
            },
            body: JSON.stringify(formData)
        }

        fetch(url, settings)
            .then(response => response.json())
            .then(data => {
                 //Si no hay ningun error se muestra un mensaje diciendo que el turno
                 //se agrego bien
                 let successAlert = '<div class="alert alert-success alert-dismissible">' +
                     '<button type="button" class="close" data-bs-dismiss="alert">&times;</button>' +
                     '<strong></strong> Turno agregado </div>'

                 document.querySelector('#response').innerHTML = successAlert;
                 document.querySelector('#response').style.display = "block";
                 resetUploadForm();

            })
            .catch(error => {
                    //Si hay algun error se muestra un mensaje diciendo que el turno
                    //no se pudo guardar y se intente nuevamente
                    let errorAlert = '<div class="alert alert-danger alert-dismissible">' +
                                     '<button type="button" class="close" data-bs-dismiss="alert">&times;</button>' +
                                     '<strong> Error intente nuevamente</strong> </div>'

                      document.querySelector('#response').innerHTML = errorAlert;
                      document.querySelector('#response').style.display = "block";
                     //se dejan todos los campos vacíos por si se quiere ingresar otro paciente
                     resetUploadForm();})
    });


    function resetUploadForm(){
        document.querySelector('#odontologo').value = "";
        document.querySelector('#paciente').value = "";
         document.querySelector('#fecha').value = "";

    }

    (function(){
        let pathname = window.location.pathname;
        if(pathname === "/"){
            document.querySelector(".nav .nav-item .dropdown-item").addClass("active");
        } else if (pathname == "/post_turnos.html") {
            $(".guardar").addClass("active")
        }
    })();
});