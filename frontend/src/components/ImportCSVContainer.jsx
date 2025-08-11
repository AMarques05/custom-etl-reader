import { useState } from "react";
import axios from "axios";
import Button from "./Button";
import Input from "./Input";


function ImportCSVContainer(props) {
  const [file, setFile] = useState(null);

  function handleFileChange(event) {
    const selectedFile = event.target.files[0];
    setFile(selectedFile);
    console.log("Selected file:", selectedFile);
  }

  async function uploadCSV() {
    if (!file) {
      console.error("No file selected");
      return;
    }

    const form = new FormData();
    form.append('file', file); // field name must be "file"
    try {
      const res = await axios.post('/api/upload', form, {
        headers: { 'Content-Type': 'multipart/form-data' }
      });

      props.onDataLoaded(res.data); 
    } catch (error) {
      console.error("Error uploading file:", error);
    }
  }

  return(
    <div className="flex flex-col justify-center items-center gap-4 col-span-3 row-span-2 bg-gray-400 p-4 rounded shadow-xl/50">
      <Input onChange={handleFileChange} />
      <Button onClick={uploadCSV} />
    </div>
   );
}

export default ImportCSVContainer;