window.addEventListener('load', function () {
  //Buscamos y obtenemos el formulario donde estan
  //los datos que el usuario pudo haber modificado el turno
  const formulario = document.querySelector('#update_turno_form');

  formulario.addEventListener('submit', function (event) {
     event.preventDefault();
    let turnoId = document.querySelector('#turno_id').value;

    //creamos un JSON que tendrá los datos del turno
    //a diferencia de un turno nuevo en este caso enviamos el id
    //para poder identificarlo y modificarlo para no cargarla como nueva
    const formData = {
      id: document.querySelector('#turno_id').value,
      fecha: document.querySelector('#fecha').value,
      odontologo:{
        id:document.querySelector('#odontologo').value,
      },
      paciente:{
          id:document.querySelector('#paciente').value,
      },
    };

    //invocamos utilizando la función fetch la API turnos con el método PUT que modificará
    //el turno que enviaremos en formato JSON
    const url = '/turnos';
    const settings = {
      method: 'PUT',
      headers: {
        'Content-Type': 'application/json',
      },
      body: JSON.stringify(formData)
    }

    fetch(url,settings)
      .then(responseData => {
        location.reload();
      })
  })
 })

  //Es la funcion que se invoca cuando se hace click sobre el id de un turno del listado
  //se encarga de llenar el formulario con los datos del turno
  //que se desea modificar
  function findBy(id) {
  console.log(id)
    const url = '/turnos/buscar/id'+"/"+id;
    const settings = {
      method: 'GET'
    }
    fetch(url,settings)
      .then(response => response.json())
      .then(data => {
        let turno = data;
        document.querySelector('#turno_id').value = turno.id;
        document.querySelector('#odontologo').value = turno.odontologo.id;
        document.querySelector('#paciente').value = turno.paciente.id;
        document.querySelector('#fecha').value = turno.fecha;

        //el formulario por default esta oculto y al editar se habilita
        document.querySelector('#div_turno_updating').style.display = "block";

      }).catch(error => {
        alert("Error: " + error);
      })
  }