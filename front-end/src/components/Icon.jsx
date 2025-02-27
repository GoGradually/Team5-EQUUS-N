import React from 'react';
import { ReactComponent as HeartActivate } from '../assets/Icons/heart/activate.svg';
import { ReactComponent as HeartDefault } from '../assets/Icons/heart/default.svg';
import { ReactComponent as BellOff } from '../assets/Icons/bell/off.svg';
import { ReactComponent as BellOn } from '../assets/Icons/bell/on.svg';
import { ReactComponent as Calendar } from '../assets/Icons/calendar.svg';
import { ReactComponent as Check } from '../assets/Icons/check.svg';
import { ReactComponent as CheckBoxNone } from '../assets/Icons/check_box/noneclick.svg';
import { ReactComponent as CheckBoxClick } from '../assets/Icons/check_box/onclick.svg';
import { ReactComponent as ChevronDown } from '../assets/Icons/chevron_down.svg';
import { ReactComponent as ChevronLeft } from '../assets/Icons/chevron_left.svg';
import { ReactComponent as ChevronRight } from '../assets/Icons/chevron_right.svg';
import { ReactComponent as ChevronUp } from '../assets/Icons/chevron_up.svg';
import { ReactComponent as Crown } from '../assets/Icons/crown.svg';
import { ReactComponent as Delete } from '../assets/Icons/delete.svg';
import { ReactComponent as DeleteSmall } from '../assets/Icons/delete_small.svg';
import { ReactComponent as Dots } from '../assets/Icons/dots.svg';
import { ReactComponent as Edit } from '../assets/Icons/edit.svg';
import { ReactComponent as Eye } from '../assets/Icons/eye.svg';
import { ReactComponent as Hamburger } from '../assets/Icons/hamburger.svg';
import { ReactComponent as Logout } from '../assets/Icons/logout.svg';
import { ReactComponent as Mail } from '../assets/Icons/mail.svg';
import { ReactComponent as People } from '../assets/Icons/people.svg';
import { ReactComponent as PlusM } from '../assets/Icons/plus_m.svg';
import { ReactComponent as PlusS } from '../assets/Icons/plus_s.svg';
import { ReactComponent as Remove } from '../assets/Icons/remove.svg';
import { ReactComponent as Send } from '../assets/Icons/send.svg';
import { ReactComponent as SwapVert } from '../assets/Icons/swap_vert.svg';
import { ReactComponent as UnfoldMore } from '../assets/Icons/unfold_more.svg';
import { ReactComponent as Logo } from '../assets/Icons/logo.svg';
import { ReactComponent as Info } from '../assets/Icons/info.svg';

import Bear from '../assets/Icons/animals/Bear.webp';
import DogFace from '../assets/Icons/animals/Dog Face.webp';
import Fish from '../assets/Icons/animals/Fish.webp';
import Fox from '../assets/Icons/animals/Fox.webp';
import Frog from '../assets/Icons/animals/Frog.webp';
import Hamster from '../assets/Icons/animals/Hamster.webp';
import Koala from '../assets/Icons/animals/Koala.webp';
import LadyBeetle from '../assets/Icons/animals/Lady Beetle.webp';
import Lion from '../assets/Icons/animals/Lion.webp';
import Chick from '../assets/Icons/animals/Chick.webp';
import MonkeyFace from '../assets/Icons/animals/Monkey Face.webp';
import MouseFace from '../assets/Icons/animals/Mouse Face.webp';
import Octopus from '../assets/Icons/animals/Octopus.webp';
import Orangutan from '../assets/Icons/animals/Orangutan.webp';
import Panda from '../assets/Icons/animals/Panda.webp';
import Parrot from '../assets/Icons/animals/Parrot.webp';
import Penguin from '../assets/Icons/animals/Penguin.webp';
import PigFace from '../assets/Icons/animals/Pig Face.webp';
import PolarBear from '../assets/Icons/animals/Polar Bear.webp';
import RabbitFace from '../assets/Icons/animals/Rabbit Face.webp';
import Rooster from '../assets/Icons/animals/Rooster.webp';
import Shark from '../assets/Icons/animals/Shark.webp';
import Skunk from '../assets/Icons/animals/Skunk.webp';
import SpoutingWhale from '../assets/Icons/animals/Spouting Whale.webp';
import Swan from '../assets/Icons/animals/Swan.webp';
import Turtle from '../assets/Icons/animals/Turtle.webp';
import Whale from '../assets/Icons/animals/Whale.webp';
import Wolf from '../assets/Icons/animals/Wolf.webp';

const webpComponent = (webpData) => {
  return (props) => <img src={webpData} {...props} />;
};

const icons = {
  heartFill: HeartActivate,
  heartDefault: HeartDefault,
  bellOff: BellOff,
  bellOn: BellOn,
  calendar: Calendar,
  check: Check,
  checkBoxNone: CheckBoxNone,
  checkBoxClick: CheckBoxClick,
  chevronDown: ChevronDown,
  chevronLeft: ChevronLeft,
  chevronRight: ChevronRight,
  chevronUp: ChevronUp,
  crown: Crown,
  delete: Delete,
  deleteSmall: DeleteSmall,
  dots: Dots,
  edit: Edit,
  eye: Eye,
  hamburger: Hamburger,
  logout: Logout,
  mail: Mail,
  people: People,
  plusM: PlusM,
  plusS: PlusS,
  remove: Remove,
  send: Send,
  swapVert: SwapVert,
  unfoldMore: UnfoldMore,
  logo: Logo,
  info: Info,

  '@animals/Bear': webpComponent(Bear),
  '@animals/DogFace': webpComponent(DogFace),
  '@animals/Fish': webpComponent(Fish),
  '@animals/Fox': webpComponent(Fox),
  '@animals/Frog': webpComponent(Frog),
  '@animals/Hamster': webpComponent(Hamster),
  '@animals/Koala': webpComponent(Koala),
  '@animals/LadyBeetle': webpComponent(LadyBeetle),
  '@animals/Lion': webpComponent(Lion),
  '@animals/MonkeyFace': webpComponent(MonkeyFace),
  '@animals/MouseFace': webpComponent(MouseFace),
  '@animals/Octopus': webpComponent(Octopus),
  '@animals/Orangutan': webpComponent(Orangutan),
  '@animals/Panda': webpComponent(Panda),
  '@animals/Parrot': webpComponent(Parrot),
  '@animals/Penguin': webpComponent(Penguin),
  '@animals/PigFace': webpComponent(PigFace),
  '@animals/PolarBear': webpComponent(PolarBear),
  '@animals/RabbitFace': webpComponent(RabbitFace),
  '@animals/Rooster': webpComponent(Rooster),
  '@animals/Shark': webpComponent(Shark),
  '@animals/Skunk': webpComponent(Skunk),
  '@animals/SpoutingWhale': webpComponent(SpoutingWhale),
  '@animals/Swan': webpComponent(Swan),
  '@animals/Turtle': webpComponent(Turtle),
  '@animals/Whale': webpComponent(Whale),
  '@animals/Wolf': webpComponent(Wolf),
  '@animals/Chick': webpComponent(Chick),
};

/**
 * 아이콘 컴포넌트
 * @param {object} props
 * @param {keyof icons} props.name
 * @param {string} props.className
 * @param {string} props.color
 * @returns {ReactElement}
 */
const Icon = ({ name, className, color }) => {
  const IconComponent = icons[name];
  if (!IconComponent) {
    console.error(`Icon "${name}" does not exist.`);
    return null;
  }

  const style =
    name === 'dots' || name === 'eye' ? { fill: color }
    : name === 'chevronLeft' ? { stroke: color }
    : { stroke: color, fill: color };

  return <IconComponent className={className} style={style} />;
};

export default Icon;
