import Button from "./Button";
import Input from "./Input";

function ImportCSVContainer() {
   return(
    <div className="flex flex-col justify-center items-center gap-4 col-span-3 row-span-2 bg-gray-400 p-4 rounded shadow-xl/50">
      <Input />
      <Button />
    </div>
   );
}

export default ImportCSVContainer;