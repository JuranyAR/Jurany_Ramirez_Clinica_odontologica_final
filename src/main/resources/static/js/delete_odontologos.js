function deleteBy(id)
{
  //con fetch invocamos a la API de odontologos con el método DELETE
  //pasandole el id en la URL
  const url = '/odontologos/'+ id;
  const settings = {
      method: 'DELETE'
  }
  fetch(url,settings)
    .then(response => response.json())

  //borrar la fila del odontologo eliminado
  let row_id =  '#doc'+id;
  document.querySelector(row_id).remove();

}