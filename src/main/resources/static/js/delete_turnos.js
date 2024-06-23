function deleteBy(id)
{
  //con fetch invocamos a la API de turnos con el método DELETE
  //pasandole el id en la URL
  const url = '/turnos/'+ id;
  const settings = {
      method: 'DELETE'
  }
  fetch(url,settings)
    .then(response => response.json())

  //borrar la fila del Paciente eliminado
  let row_id =  '#turn'+id;
  document.querySelector(row_id).remove();

}