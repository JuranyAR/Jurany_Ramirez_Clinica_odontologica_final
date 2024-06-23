function deleteBy(id)
{
  //con fetch invocamos a la API de pacientes con el método DELETE
  //pasandole el id en la URL
  const url = '/pacientes/'+ id;
  const settings = {
      method: 'DELETE'
  }
  fetch(url,settings)
    .then(response => response.json())

  //borrar la fila del Paciente eliminado
  let row_id =  '#pac'+id;
  document.querySelector(row_id).remove();

}