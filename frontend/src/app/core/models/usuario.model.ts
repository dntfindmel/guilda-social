export interface Usuario {
  id?: string;
  nome: string;
  email: string;
  dataNascimento: Date;
  telefone?: string;
  cidade: string;
  estado: string;
  latitude?: number;
  longitude?: number;
  dataCadastro?: Date;
  ativo?: boolean;
}

export interface Jogo {
  id: string;
  nome: string;
  tipo: 'TABULEIRO' | 'CARD_GAME' | 'RPG_MESA' | 'WAR_GAME';
  minJogadores: number;
  maxJogadores: number;
  tempoMedioPartida?: number;
  imagemUrl?: string;
}
