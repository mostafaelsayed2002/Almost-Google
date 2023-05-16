import { FeatureCard } from "@/components/FeaturesCard";
import { Logo } from "@/components/Logo";

interface creator {
  name: string;
  text: string;
  img: string; 
  twitter?: string; 
  github?: string;
  linkedin?: string;
}
const FeatureCreator =  ({name, text, img, twitter, github, linkedin} : creator) => {
  return (
    <FeatureCard
      title={name}
      text={text}
      image={<img src={img} alt={name} className="w-200" />}
      children={
        <div className="flex flex-row items-center gap-2">
          <a
            hidden={linkedin === undefined}
            href={linkedin}
            target="_blank"
            rel="noopener noreferrer"
          >
            <svg
              viewBox="0 0 16 17"
              fill="none"
              xmlns="http://www.w3.org/2000/svg"
              className="w-6"
            >
              <path
                d="M14.0953 0.571411H1.90476C0.853333 0.571411 0 1.42474 0 2.47617V14.6667C0 15.7181 0.853333 16.5714 1.90476 16.5714H14.0953C15.1467 16.5714 16 15.7181 16 14.6667V2.47617C16 1.42474 15.1467 0.571411 14.0953 0.571411ZM4.95238 6.66665V13.9047H2.66667V6.66665H4.95238ZM2.66667 4.55998C2.66667 4.02665 3.12381 3.61903 3.80953 3.61903C4.49524 3.61903 4.92571 4.02665 4.95238 4.55998C4.95238 5.09332 4.52571 5.52379 3.80953 5.52379C3.12381 5.52379 2.66667 5.09332 2.66667 4.55998ZM13.3333 13.9047H11.0476C11.0476 13.9047 11.0476 10.3771 11.0476 10.0952C11.0476 9.33334 10.6667 8.57141 9.71427 8.55614H9.6838C8.76193 8.55614 8.38093 9.34094 8.38093 10.0952C8.38093 10.4419 8.38093 13.9047 8.38093 13.9047H6.09524V6.66665H8.38093V7.64188C8.38093 7.64188 9.1162 6.66665 10.5943 6.66665C12.1067 6.66665 13.3333 7.70668 13.3333 9.81334V13.9047Z"
                fill="white"
                fill-opacity="0.5"
              />
            </svg>
          </a>
          <a
            hidden={twitter === undefined}
            href={twitter}
            target="_blank"
            rel="noopener noreferrer"
          >
            <svg
              viewBox="0 0 16 15"
              fill="none"
              xmlns="http://www.w3.org/2000/svg"
              className="w-6"
            >
              <path
                d="M16 2.19572C15.4111 2.47905 14.7785 2.66972 14.1145 2.75572C14.7927 2.31572 15.3127 1.61905 15.5575 0.788381C14.9237 1.19572 14.221 1.49172 13.4727 1.65172C12.8739 0.960384 12.0211 0.528381 11.0771 0.528381C9.26427 0.528381 7.7948 2.12105 7.7948 4.08438C7.7948 4.36305 7.82433 4.63505 7.87967 4.89438C5.1518 4.74638 2.73343 3.33038 1.11381 1.17838C0.831967 1.70372 0.670127 2.31438 0.670127 2.96705C0.670127 4.20038 1.24918 5.28905 2.12977 5.92638C1.59194 5.90772 1.0855 5.74772 0.643052 5.48172C0.643052 5.49705 0.643052 5.51105 0.643052 5.52638C0.643052 7.24972 1.77409 8.68706 3.27618 9.01306C3.00111 9.09439 2.71067 9.13772 2.41099 9.13772C2.19991 9.13772 1.99377 9.11506 1.79378 9.07439C2.21161 10.4871 3.42387 11.5157 4.86012 11.5444C3.73709 12.4984 2.32176 13.0671 0.783353 13.0671C0.518749 13.0671 0.257221 13.0504 0 13.0171C1.45287 14.0257 3.17773 14.6144 5.03181 14.6144C11.0697 14.6144 14.3705 9.19572 14.3705 4.49638C14.3705 4.34238 14.3675 4.18905 14.3613 4.03638C15.0031 3.53438 15.56 2.90838 16 2.19572Z"
                fill="white"
                fill-opacity="0.5"
              />
            </svg>
          </a>
          <a
            hidden={github === undefined}
            href={github}
            target="_blank"
            rel="noopener noreferrer"
          >
            <svg
              viewBox="0 0 16 17"
              fill="none"
              xmlns="http://www.w3.org/2000/svg"
              className="w-6"
            >
              <path
                d="M7.102 0.855595C3.4145 1.2564 0.448494 4.22242 0.0476819 7.82972C-0.35313 11.5973 1.81125 14.9642 5.09791 16.2468C5.3384 16.3269 5.57889 16.1666 5.57889 15.846V14.5634C5.57889 14.5634 5.25824 14.6435 4.85743 14.6435C3.73515 14.6435 3.25418 13.6816 3.17401 13.1205C3.09385 12.7998 2.93353 12.5593 2.69304 12.3188C2.45255 12.2387 2.37239 12.2387 2.37239 12.1585C2.37239 11.9982 2.61288 11.9982 2.69304 11.9982C3.17401 11.9982 3.57483 12.5593 3.73515 12.7998C4.13597 13.4411 4.61694 13.6014 4.85743 13.6014C5.17807 13.6014 5.41856 13.5213 5.57889 13.4411C5.65905 12.8799 5.89954 12.3188 6.38051 11.9982C4.53677 11.5973 3.17401 10.5553 3.17401 8.79166C3.17401 7.90986 3.57483 7.0281 4.13597 6.3868C4.0558 6.22648 3.97564 5.82566 3.97564 5.26452C3.97564 4.94388 3.97564 4.4629 4.21613 3.98193C4.21613 3.98193 5.3384 3.98193 6.46067 5.02404C6.86147 4.86372 7.4226 4.78355 7.98373 4.78355C8.54487 4.78355 9.106 4.86372 9.587 5.02404C10.6291 3.98193 11.8315 3.98193 11.8315 3.98193C11.9919 4.4629 11.9919 4.94388 11.9919 5.26452C11.9919 5.90582 11.9117 6.22648 11.8315 6.3868C12.3927 7.0281 12.7935 7.82972 12.7935 8.79166C12.7935 10.5553 11.4307 11.5973 9.587 11.9982C10.068 12.399 10.3886 13.1205 10.3886 13.8419V15.9261C10.3886 16.1666 10.6291 16.4071 10.9498 16.3269C13.9158 15.1245 16 12.2387 16 8.87186C16 4.06209 11.9117 0.294457 7.102 0.855595Z"
                fill="white"
                fill-opacity="0.5"
              />
            </svg>
          </a>
        </div>
      }
    />
  );
}

export default function AboutUs() {
  return (
    <div className="flex flex-col gap-16 py-16">


      <section className="flex flex-col lg:flex-row justify-between gap-4">
        <section className="flex flex-col gap-4 mt-24 ">
          <h1 className="font-playfair text-white text-[36px] text-center lg:text-left">Who Are We?</h1>
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
        <img src="https://i.imgur.com/V4l33ka.png" className="lg:max-w-[500px] lg:w-fit w-full" />
      </section>

      <section className="flex flex-col gap-4 items-center w-full">
        <h1 className="font-playfair text-white text-[36px] lg:text-left text-center">
          Who are the people behind Almost Google?
        </h1>
        <section className="flex lg:flex-row flex-col gap-4 text-center w-full">
          <FeatureCreator name="Mohamed Maher" text="Developer" img="https://i.imgur.com/1rwZwc7.png" github=" " linkedin=" " twitter=" "
          />
          <FeatureCreator name="Mostafa Magdy" text="Mango" img="https://i.imgur.com/NjR7kS3.png" github=" " linkedin=" " twitter=" "
          />
          <FeatureCreator name="Walid Khamees" text="Strange man" img="https://i.imgur.com/ystYvl5.png" github=" " linkedin=" " twitter=" "
          />
          <FeatureCreator name="Mostafa Elsayed" text="Ghost Buster" img="https://i.imgur.com/UxYzjMq.png" github=" " linkedin=" " twitter=" " />"
        </section>
      </section>
    </div>
  );
}
