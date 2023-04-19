import { FeatureCard } from "@/components/FeaturesCard";
import { Logo } from "@/components/Logo";

export default function AboutUs() {
  return (
    <div className="flex flex-col gap-16 py-16">
      <section className="flex flex-col gap-8 w-full items-center">
        <Logo />
      </section>

      <section className="flex flex-row  justify-between">
        <section className="flex flex-col gap-4 mt-24 ">
          <h1 className="font-playfair text-white text-[36px]">Who Are We?</h1>
          <p className="p text-left">
            We're the team behind Almost Google, and we're still figuring it out
            ourselves! We're not your typical search engine, and we don't claim
            to have all the answers. Our search results might surprise you, or
            they might not quite hit the mark. We're here to embrace the beauty
            of uncertainty and the joy of serendipity. Join us as we explore the
            unknown and deliver a search experience that's anything but
            predictable. Welcome to Almost Google, where we're still figuring it
            out, and that's the fun part! Let's dive in and explore together!
          </p>
        </section>
        <img src="https://i.imgur.com/V4l33ka.png"></img>
      </section>
      
    <section className="flex flex-col gap-4 items-center">
        <h1 className="font-playfair text-white text-[36px]">Who are the people behind Almost Google?</h1>

      <section className="flex flex-row gap-4 text-center">

      <FeatureCard
          title="Mohamed Maher"
          text="Developer"
          image={
            <img
              src="https://i.imgur.com/1rwZwc7.png"
              alt="Mohamed Maher"
              className="w-200"
            />
          }
          children={
            <div className="flex flex-row gap-2">
           <img className="w-7" src="https://i.imgur.com/xTNd7t3.png"></img>
           <img className="w-7" src="https://i.imgur.com/u59BZZ0.png"></img>
           <img className="w-7" src="https://i.imgur.com/BiUj69N.png"></img>
           </div>
           }
        />
              <FeatureCard
          title="Mostafa Magdy"
          text="Mango"
          image={
            <img
              src="https://i.imgur.com/NjR7kS3.png"
              alt="Mostafa Magdy"
              className="w-200"
            />
          }
          children={
            <div className="flex flex-row gap-2">
           <img className="w-7" src="https://i.imgur.com/xTNd7t3.png"></img>
           <img className="w-7" src="https://i.imgur.com/u59BZZ0.png"></img>
           <img className="w-7" src="https://i.imgur.com/BiUj69N.png"></img>
           </div>
           }
        />
              <FeatureCard
          title="Walid Khammees"
          text="Strange man"
          image={
            <img
              src="https://i.imgur.com/ystYvl5.png"
              alt="Walid Khammees"
              className="w-200"
            />
          }
          children={
            <div className="flex flex-row gap-2">
           <img className="w-7" src="https://i.imgur.com/xTNd7t3.png"></img>
           <img className="w-7" src="https://i.imgur.com/u59BZZ0.png"></img>
           <img className="w-7" src="https://i.imgur.com/BiUj69N.png"></img>
           </div>
           }

        />
              <FeatureCard
          title="Mostafa Elsayed"
          text="Ghost Buster"
          image={
            <img
              src="https://i.imgur.com/UxYzjMq.png"
              alt="Mostafa Elsayed"
              className="w-200"
            />
          }
         children={
          <div className="flex flex-row gap-2">
         <img className="w-7" src="https://i.imgur.com/xTNd7t3.png"></img>
         <img className="w-7" src="https://i.imgur.com/u59BZZ0.png"></img>
         <img className="w-7" src="https://i.imgur.com/BiUj69N.png"></img>
         </div>
         }

        />



      </section>



    </section>



    </div>
  );
}
