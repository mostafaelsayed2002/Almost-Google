import { Logo } from "@/components/Logo";
import { Result } from "@/components/Result";
import { SearchBar } from "@/components/SearchBar";

export const Resultpage = () => {
    return (
      
        <div className="flex flex-col items-center">
       
               <Logo/>
               <div className="w-full mt-8"><SearchBar/></div>
               <div className="flex flex-col w-full  mt-20 gap-4">
                <Result/>
                <Result/>
                <Result/>
                <Result/>
                <Result/>
               </div>

              <div className="flex flex-row gap-4 mt-8">
               <button className="bg-black text-white rouned rounded-lg text-center py-2 px-4 border ">1</button>
               <button className="bg-black text-body  rouned rounded-lg text-center py-2 px-4 border border-body ">2</button>
               <button className="bg-black text-body  rouned rounded-lg text-center py-2 px-4 border border-body ">3</button>
              </div>
               <h3 className="h3 place-self-center mt-16">About Us</h3>
        </div>
    );
  };
  