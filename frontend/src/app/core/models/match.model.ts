export interface Sugestao {
  id: string;
  nome: string;
  cidade: string;
  estado: string;
  estiloJogo?: string;
  descricao?: string;
  fotoPerfil?: string;
  nivelAfinidade: number;
  nivel: number;      // Nível do jogador (fixo)
  distancia: number;  // Distância em km (fixa)
  idade: number;      // Idade do jogador (fixa)
  tags: string[];     // Tags de interesse
}

export interface Match {
  id: string;
  usuarioId: string;
  usuarioNome: string;
  nivelAfinidade: number;
  status: 'PENDENTE' | 'ACEITO' | 'RECUSADO';
  dataMatch: Date;
}
